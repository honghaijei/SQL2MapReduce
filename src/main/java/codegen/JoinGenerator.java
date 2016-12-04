package codegen;

import astree.ArithNode;
import astree.FilterNode;
import astree.TreeNode;
import common.schema.DataType;
import common.schema.Schema;
import dag.JoinGraphNode;

import java.util.Arrays;
import java.util.List;

import static codegen.Helper.JoinFunction;

/**
 * Created by honghaijie on 11/15/16.
 */
public class JoinGenerator {
    public JoinGenerator(JoinGraphNode node) {
        this.node = node;
        this.schema1 = node.GetInputSchemas().get(0);
        this.schema2 = node.GetInputSchemas().get(1);
        this.input1 = node.GetInputs().get(0);
        this.input2 = node.GetInputs().get(1);
        this.output = node.GetOutput();
        this.joinColTypes = new DataType[]{ schema1.Get(schema1.GetPosByName(node.GetLeftKey())).type };
        this.joinColLeftIndex = new int[] { schema1.GetPosByName(node.GetLeftKey()) };
        this.joinColRightIndex = new int[] { schema2.GetPosByName(node.GetRightKey()) };
    }
    public String Generate() {
        return Helper.Import +
                "public class " + this.node.GetName() + " {\n" +
                Helper.SplitFunction +
                Helper.JoinFunction +
                new CompositeKeyGenerator(joinColTypes).Generate() +
                JoinMapper() +
                JoinReducer() +
                JoinMain() +
                "}"
                ;
    }
    public String JoinSetup() {
        return "        private int table = 0;\n" +
                "        public void setup(Context context) throws IOException, InterruptedException {\n" +
                "            String path = ((FileSplit)context.getInputSplit()).getPath().toString();\n" +
                "            if (path.contains(\"" + input2 + "\"))\n" +
                "                table = 1;\n" +
                "        }";
    }
    public String JoinMapper() {
        List<TreeNode> filters = node.GetMapperFilter();
        return "    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{\n" +
                JoinSetup() +
                "        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{\n" +
                "            String line = value.toString();\n" +
                "            if (table == 0) {\n" +
                "                String[] arr = Split(line, '|',  " + schema1.size() + ");\n" +
                "                if (" + new WhereGenerator(filters.get(0), Arrays.asList(node.GetInputSchemas().get(0))).Generate() + ") {\n" +
                "                    context.write(new CompositeKey(" + JoinMapperLeftCompositeArgs(joinColLeftIndex, schema1) + "), new Text(table + \"|\" + line));\n" +
                "                }" +
                "            } else {\n" +
                "                String[] arr = Split(line, '|', " + schema2.size() + ");\n" +
                "                if (" + new WhereGenerator(filters.get(1), Arrays.asList(node.GetInputSchemas().get(1))).Generate() + ") {\n" +
                "                    context.write(new CompositeKey(" + JoinMapperLeftCompositeArgs(joinColRightIndex, schema2) + "), new Text(table + \"|\" + line));\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }";
    }
    String JoinMapperLeftCompositeArgs(int[] joinColIndex, Schema schema) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < joinColIndex.length; ++i) {
            int pos = joinColIndex[i];
            sb.append(Helper.ParseString2JavaType("arr[" + pos + "]", schema.Get(pos).type));
            if (i != joinColIndex.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    public String JoinReducer() {
        return "    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{\n" +
                "        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{\n" +
                "            NullWritable t = NullWritable.get();\n" +
                "            List<String[]> leftTuples = new ArrayList();\n" +
                "            List<String[]> rightTuples = new ArrayList();\n" +
                "\n" +
                "            for (Text value : values) {\n" +
                "                String s = value.toString();\n" +
                "                String[] arr = Split(s, '|', 2);\n" +
                "                int table = Integer.parseInt(arr[0]);\n" +
                "                if (table == 0) {\n" +
                "                    leftTuples.add(arr[1].split(\"\\\\|\"));\n" +
                "                } else if (table == 1) {\n" +
                "                    rightTuples.add(arr[1].split(\"\\\\|\"));\n" +
                "                }\n" +
                "            }\n" +
                "            for (String[] arr1 : leftTuples) {\n" +
                "                for (String[] arr2 : rightTuples) {\n" +
                ReducerFilter() +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }";
    }
    public String ReducerFilter() {
        if (node.GetReducerFilter() == null) {
            return "                    context.write(t, new Text(" + OutputColumn() + "));\n";
        }
        return "                    String[] arr = new String[]{" +
                OutputColumnArr() +
                "};\n" +
                "                    if (" +
                new WhereGenerator(node.GetReducerFilter(), Arrays.asList(node.GetOutputSchema())).Generate() +
                ") {\n" +
                "                        context.write(t, new Text(String.join(\"|\", arr)));\n" +
                "                    }";
    }
    public String OutputColumnArr() {
        StringBuilder sb = new StringBuilder();
        if (node.GetOutputColumns() == null) {
            for (int i = 0; i < schema1.size(); ++i) {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append("arr1[" + i + "]");
            }
            for (int i = 0; i < schema2.size(); ++i) {
                sb.append(",");
                sb.append("arr2[" + i + "]");
            }
        }
        return sb.toString();
    }
    public String OutputColumn() {
        StringBuilder sb = new StringBuilder();
        if (node.GetOutputColumns() == null) {
            for (int i = 0; i < schema1.size(); ++i) {
                if (i != 0) {
                    sb.append(" + '|' + ");
                }
                sb.append("arr1[" + i + "]");
            }
            for (int i = 0; i < schema2.size(); ++i) {
                sb.append(" + '|' + ");
                sb.append("arr2[" + i + "]");
            }
        } else {
            boolean first = true;
            for (ArithNode anode : node.GetOutputColumns()) {
                if (!first) {
                    sb.append(" + '|' + ");
                }
                sb.append("((Object)(");
                String t = new ArithExpressionGenerator(anode, node.GetInputSchemas(), Arrays.asList("arr")).Generate();
                sb.append(t);
                sb.append(")).toString()");
                first = false;
            }
        }
        return sb.toString();
    }
    public String JoinMain() {
        return "    public static void main(String[] args) throws Exception{\n" +
                "        Configuration conf = new Configuration();\n" +
                "        Job job = new Job(conf,\"Sort\");\n" +
                "        job.setJarByClass(" + this.node.GetName() + ".class);\n" +
                "        job.setMapperClass(Map.class);\n" +
                "        job.setReducerClass(Reduce.class);\n" +
                "        job.setMapOutputKeyClass(CompositeKey.class);\n" +
                "        job.setOutputValueClass(Text.class);\n" +
                "        FileInputFormat.addInputPath(job, new Path(\"" + input1 + "\"));\n" +
                "        FileInputFormat.addInputPath(job, new Path(\"" + input2 + "\"));\n" +
                "        FileOutputFormat.setOutputPath(job, new Path(\"" + output + "\"));\n" +
                "        System.exit(job.waitForCompletion(true) ? 0 : 1);\n" +
                "    }";
    }
    private JoinGraphNode node;
    private Schema schema1;
    private Schema schema2;
    private String input1;
    private String input2;
    private String output;
    private DataType[] joinColTypes;
    private int[] joinColLeftIndex;
    private int[] joinColRightIndex;
}
