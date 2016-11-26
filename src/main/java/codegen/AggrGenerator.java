package codegen;

import common.AggrFunction;
import common.schema.DataType;
import common.schema.Schema;
import astree.ArithNode;
import dag.AggrGraphNode;

import java.util.*;

/**
 * Created by honghaijie on 11/12/16.
 */
public class AggrGenerator {
    public AggrGenerator(AggrGraphNode node) {
        this.node = node;
        this.schema = node.GetInputSchemas().get(0);
        this.cols = node.GetGroupByKeys();
        int sz = cols.size();
        groupByColIndex = new int[sz];
        groupByColTypes = new DataType[sz];
        int i = 0;
        for (String colName : cols) {
            int idx = schema.GetPosByName(colName);
            groupByColIndex[i] = idx;
            groupByColTypes[i] = schema.Get(idx).type;
            ++i;
        }
        this.input = node.GetInputs().get(0);
        this.output = node.GetOutput();
    }
    public String Generate() {
        return Helper.Import +
                "public class Main {\n" +
                Helper.SplitFunction +
                new CompositeKeyGenerator(groupByColTypes).Generate() +
                AggrMapper() +
                AggrReducer() +
                AggrByMain() +
                "}"
                ;
    }
    public String AggrMapper() {
        return "    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{\n" +
                "        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{\n" +
                "            String line = value.toString();\n" +
                "            String[] arr = Split(line, '|', " + schema.size() + ");\n" +
                "            context.write(new CompositeKey(" +
                GroupByMapperCompositeArgs() +
                "), new Text(line));\n" +
                "        }\n" +
                "    }\n";
    }
    public String AggrReducer() {
        int vid = 0;
        Map<ArithNode, String> arithNameMapper = new HashMap<>();
        for (ArithNode node : this.node.GetOutputColumns()) {
            ArithExpressionGenerator aeg = new ArithExpressionGenerator(node, Arrays.asList(schema), Arrays.asList("arr"));
            List<ArithNode> internalNodes = aeg.GetAggrInternal();
            for (ArithNode internalNode : internalNodes) {
                arithNameMapper.put(internalNode, "agg" + vid);
                ++vid;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{\n" +
                "        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{\n" +
                "            NullWritable t = NullWritable.get();\n");

        for (ArithNode node : arithNameMapper.keySet()) {
            sb.append(Helper.Spaces(12) +
                    Helper.DataType2JavaType(node.type) +
                    " " +
                    arithNameMapper.get(node) +
                    " = " +
                    Helper.DefaultValue(node.type) +
                    ";\n");
        }
        sb.append("            int count = 0;\n");
        sb.append("            String[] arr = null;\n");
        sb.append("            for (Text value : values) {\n" +
                "                arr = Split(value.toString(), '|', " + schema.size() + ");\n");
        sb.append("                ++count;\n");
        List<AggrFunction> afList = new ArrayList<>();
        StringBuilder external = new StringBuilder();
        for (ArithNode node : arithNameMapper.keySet()) {
            List<DataType> argsTypes = new ArrayList<>();
            List<String> argsExpression = new ArrayList<>();
            for (ArithNode ch : node.children) {
                argsTypes.add(ch.type);
                argsExpression.add(new ArithExpressionGenerator(ch, Arrays.asList(schema), Arrays.asList("arr")).Generate());
            }
            AggrFunction f = new AggrFunction(node.function, argsTypes);
            sb.append(Helper.Spaces(16) + f.GenerateInternal(arithNameMapper.get(node), argsExpression));
            external.append(f.GenerateExternal(arithNameMapper.get(node), "count"));
            sb.append("\n");
            afList.add(f);
        }
        sb.append("            }\n");
        sb.append(Helper.Spaces(12) + external.toString());

        for (ArithNode node : arithNameMapper.keySet()) {
            node.constant = arithNameMapper.get(node);
            node.function = null;
        }
        sb.append("            context.write(t, ");
        sb.append("new Text(");
        for (int i = 0; i < node.GetOutputColumns().size(); ++i) {
            ArithNode node = this.node.GetOutputColumns().get(i);
            ArithExpressionGenerator aeg = new ArithExpressionGenerator(node, Arrays.asList(schema), Arrays.asList("arr"));
            if (i != 0) {
                sb.append("+\"|\"+");
            }
            sb.append("((Object)(" + aeg.Generate() + ")).toString()");
        }
        sb.append("));\n");
        sb.append("        }\n");
        sb.append("    }\n");
        return sb.toString();
    }
    String GroupByMapperCompositeArgs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groupByColIndex.length; ++i) {
            int pos = groupByColIndex[i];
            sb.append(Helper.ParseString2JavaType("arr[" + pos + "]", schema.Get(pos).type));
            if (i != groupByColIndex.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    String AggrByMain() {
        return "    public static void main(String[] args) throws Exception{\n" +
                "        Configuration conf = new Configuration();\n" +
                "        Job job = new Job(conf,\"Sort\");\n" +
                "        job.setJarByClass(Main.class);\n" +
                "        job.setMapperClass(Map.class);\n" +
                "        job.setReducerClass(Reduce.class);\n" +
                "        job.setMapOutputKeyClass(CompositeKey.class);\n" +
                "        job.setOutputValueClass(Text.class);\n" +
                "        FileInputFormat.addInputPath(job, new Path(\"" + input + "\"));\n" +
                "        FileOutputFormat.setOutputPath(job, new Path(\"" + output + "\"));\n" +
                "        System.exit(job.waitForCompletion(true) ? 0 : 1);\n" +
                "    }\n" +
                "\n";
    }
    private AggrGraphNode node;
    private Schema schema;
    private List<String> cols;
    private int[] groupByColIndex;
    private DataType[] groupByColTypes;
    private String input;
    private String output;
}
