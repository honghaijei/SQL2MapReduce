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

public class SORT1 {
    public static class CompositeKey implements WritableComparable<CompositeKey> {
        public CompositeKey() {}
        public CompositeKey(Double k0) {
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
            dataOutput.writeDouble(k0);
        }
        public void readFields(DataInput dataInput) throws IOException {
            k0=dataInput.readDouble();
        }
        public Double k0;
    }
    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{
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
    }        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
            String line = value.toString();
            String[] arr = Split(line, '|', 8);
            context.write(new CompositeKey(Double.parseDouble(arr[2])), new Text(line));
        }
    }
    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{
        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{
            NullWritable t = NullWritable.get();
            for (Text value : values) {
                context.write(t, value);
            }
        }
    }
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = new Job(conf,"Sort");
        job.setJarByClass(SORT1.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(CompositeKey.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(1);
        FileInputFormat.addInputPath(job, new Path("AGG1"));
        FileOutputFormat.setOutputPath(job, new Path("SORT1"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
