package codegen;

import common.AggrFunction;
import common.schema.DataType;
import common.schema.Schema;
import tree.ArithNode;
import tree.GroupByNode;
import tree.SelectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by honghaijie on 11/12/16.
 */
public class AggrGenerator {
    public AggrGenerator(SelectNode select, Schema schema, String input, String output) {
        this.select = select;
        this.schema = schema;
        GroupByNode node = select.groupby;
        groupByColIndex = new int[node.cols.size()];
        groupByColTypes = new DataType[node.cols.size()];
        int i = 0;
        for (String colName : node.cols) {
            int idx = schema.FindByName(colName);
            groupByColIndex[i] = idx;
            groupByColTypes[i] = schema.Get(idx).type;
            ++i;
        }
        this.input = input;
        this.output = output;
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
        for (ArithNode node : select.columns) {
            ArithExpressionGenerator aeg = new ArithExpressionGenerator(node, schema);
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
                argsExpression.add(new ArithExpressionGenerator(ch, schema).Generate());
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
        for (int i = 0; i < select.columns.size(); ++i) {
            ArithNode node = select.columns.get(i);
            ArithExpressionGenerator aeg = new ArithExpressionGenerator(node, schema);
            if (i != 0) {
                sb.append("+\"|\"+");
            }
            sb.append("((Object)(" + aeg.Generate() + ")).toString())");
        }
        sb.append(");\n");
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
    private SelectNode select;
    private Schema schema;
    private int[] groupByColIndex;
    private DataType[] groupByColTypes;
    private String input;
    private String output;
}
