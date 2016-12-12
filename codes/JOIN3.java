import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class JOIN3 {
    public static String[] Split(String s, char seperator, int expected_length) {
        String[] code = new String[expected_length];
        int pos = 0;
        for(int i = 0;;) {
            int nx = s.indexOf(seperator, pos);
            if (nx == -1 || i == expected_length - 1) nx = s.length();
            code[i++] = s.substring(pos, nx);
            if (nx == s.length()) break;
            pos = nx + 1;
        };
        return code;
    }    public static String Join(String sep, String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            if (i != 0) {
                sb.append(sep);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }    public static class CompositeKey implements WritableComparable<CompositeKey> {
        public CompositeKey() {}
        public CompositeKey(Integer k0) {
            this.k0= k0;
        }
        public int compareTo(CompositeKey y) {
            if (k0.equals(y.k0)) {
                return 0;
            } else {
                return k0.compareTo(y.k0);
            }
        }
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeInt(k0);
        }
        public void readFields(DataInput dataInput) throws IOException {
            k0=dataInput.readInt();
        }
        public Integer k0;
    }
    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{
        private int table = 0;
        public void setup(Context context) throws IOException, InterruptedException {
            String path = ((FileSplit)context.getInputSplit()).getPath().toString();
            if (path.contains("JOIN2"))
                table = 1;
        }        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
            String line = value.toString();
            if (table == 0) {
                String[] arr = Split(line, '|',  12);
                if ((true)) {
                    context.write(new CompositeKey(Integer.parseInt(arr[0])), new Text(table + "|" + line));
                }            } else {
                String[] arr = Split(line, '|', 25);
                if ((true)) {
                    context.write(new CompositeKey(Integer.parseInt(arr[17])), new Text(table + "|" + line));
                }
            }
        }
    }    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{
        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{
            NullWritable t = NullWritable.get();
            List<String[]> leftTuples = new ArrayList();
            List<String[]> rightTuples = new ArrayList();

            for (Text value : values) {
                String s = value.toString();
                String[] arr = Split(s, '|', 2);
                int table = Integer.parseInt(arr[0]);
                if (table == 0) {
                    leftTuples.add(arr[1].split("\\|"));
                } else if (table == 1) {
                    rightTuples.add(arr[1].split("\\|"));
                }
            }
            for (String[] arr1 : leftTuples) {
                for (String[] arr2 : rightTuples) {
                    context.write(t, new Text(arr1[0] + '|' + arr1[1] + '|' + arr1[2] + '|' + arr1[3] + '|' + arr1[4] + '|' + arr1[5] + '|' + arr1[6] + '|' + arr1[7] + '|' + arr1[8] + '|' + arr1[9] + '|' + arr1[10] + '|' + arr1[11] + '|' + arr2[0] + '|' + arr2[1] + '|' + arr2[2] + '|' + arr2[3] + '|' + arr2[4] + '|' + arr2[5] + '|' + arr2[6] + '|' + arr2[7] + '|' + arr2[8] + '|' + arr2[9] + '|' + arr2[10] + '|' + arr2[11] + '|' + arr2[12] + '|' + arr2[13] + '|' + arr2[14] + '|' + arr2[15] + '|' + arr2[16] + '|' + arr2[17] + '|' + arr2[18] + '|' + arr2[19] + '|' + arr2[20] + '|' + arr2[21] + '|' + arr2[22] + '|' + arr2[23] + '|' + arr2[24]));
                }
            }
        }
    }    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = new Job(conf,"Sort");
        job.setJarByClass(JOIN3.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(CompositeKey.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path("JOIN1"));
        FileInputFormat.addInputPath(job, new Path("JOIN2"));
        FileOutputFormat.setOutputPath(job, new Path("JOIN3"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }}
