package codegen;

import common.schema.DataType;
import common.schema.Schema;
import tree.OrderByNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/11/16.
 */
public class OrderByGenerator {
    public OrderByGenerator(OrderByNode node, Schema schema, String input, String output) {
        this.schema = schema;
        orderByColIndex = new int[node.cols.size()];
        orderByColTypes = new DataType[node.cols.size()];
        int i = 0;
        for (String colName : node.cols) {
            int idx = schema.FindByName(colName);
            orderByColIndex[i] = idx;
            orderByColTypes[i] = schema.Get(idx).type;
            ++i;
        }
        this.input = input;
        this.output = output;
    }
    public String Generate() {
        return Helper.Import +
                "public class Main {\n" +
                new CompositeKeyGenerator(orderByColTypes).Generate() +
                OrderByMapper() +
                OrderByReducer() +
                OrderByMain() +
                "}"
                ;
    }
    String OrderByMapper() {
        return "    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{\n" +
                Helper.SplitFunction +
                "        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{\n" +
                "            String line = value.toString();\n" +
                "            String[] arr = Split(line, '|', " + schema.size() + ");\n" +
                "            context.write(new CompositeKey(" +
                OrderByMapperCompositeArgs() +
                "), new Text(line));\n" +
                "        }\n" +
                "    }\n";
    }
    String OrderByReducer() {
        return "    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{\n" +
                "        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{\n" +
                "            NullWritable t = NullWritable.get();\n" +
                "            for (Text value : values) {\n" +
                "                context.write(t, value);\n" +
                "            }\n" +
                "        }\n" +
                "    }\n";
    }
    String OrderByMapperCompositeArgs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderByColIndex.length; ++i) {
            int pos = orderByColIndex[i];
            sb.append(Helper.ParseString2JavaType("arr[" + pos + "]", schema.Get(pos).type));
            if (i != orderByColIndex.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    String OrderByMain() {
        return "    public static void main(String[] args) throws Exception{\n" +
                "        Configuration conf = new Configuration();\n" +
                "        Job job = new Job(conf,\"Sort\");\n" +
                "        job.setJarByClass(Main.class);\n" +
                "        job.setMapperClass(Map.class);\n" +
                "        job.setReducerClass(Reduce.class);\n" +
                "        job.setMapOutputKeyClass(CompositeKey.class);\n" +
                "        job.setOutputValueClass(Text.class);\n" +
                "        job.setNumReduceTasks(1);\n" +
                "        FileInputFormat.addInputPath(job, new Path(\"" + input + "\"));\n" +
                "        FileOutputFormat.setOutputPath(job, new Path(\"" + output + "\"));\n" +
                "        System.exit(job.waitForCompletion(true) ? 0 : 1);\n" +
                "    }\n" +
                "\n";
    }
    private Schema schema;
    private int[] orderByColIndex;
    private DataType[] orderByColTypes;
    private String input;
    private String output;

    public static void main(String[] args) {
        Schema s = new Schema("");
        s.Add("Name", DataType.STRING);
        s.Add("Age", DataType.INT32);
        s.Add("Height", DataType.DOUBLE);

        OrderByNode node = new OrderByNode();
        node.cols.add("Name");
        System.out.println(new OrderByGenerator(node, s, "input", "output").Generate());
    }
}
