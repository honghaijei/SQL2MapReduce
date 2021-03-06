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

public class AGG1 {
    public static String Join(String sep, String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            if (i != 0) {
                sb.append(sep);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }    public static String[] Split(String s, char seperator, int expected_length) {
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
    }    public static class CompositeKey implements WritableComparable<CompositeKey> {
        public CompositeKey() {}
        public CompositeKey(Integer k0, String k1, Double k2, String k3, String k4, String k5, String k6) {
            this.k0= k0;
            this.k1= k1;
            this.k2= k2;
            this.k3= k3;
            this.k4= k4;
            this.k5= k5;
            this.k6= k6;
        }
        public int compareTo(CompositeKey y) {
            if (k0.equals(y.k0)) {
                if (k1.equals(y.k1)) {
                    if (k2.equals(y.k2)) {
                        if (k3.equals(y.k3)) {
                            if (k4.equals(y.k4)) {
                                if (k5.equals(y.k5)) {
                                    if (k6.equals(y.k6)) {
                                        return 0;
                                    } else {
                                        return k6.compareTo(y.k6);
                                    }
                                } else {
                                    return k5.compareTo(y.k5);
                                }
                            } else {
                                return k4.compareTo(y.k4);
                            }
                        } else {
                            return k3.compareTo(y.k3);
                        }
                    } else {
                        return k2.compareTo(y.k2);
                    }
                } else {
                    return k1.compareTo(y.k1);
                }
            } else {
                return k0.compareTo(y.k0);
            }
        }
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeInt(k0);
            dataOutput.writeUTF(k1);
            dataOutput.writeDouble(k2);
            dataOutput.writeUTF(k3);
            dataOutput.writeUTF(k4);
            dataOutput.writeUTF(k5);
            dataOutput.writeUTF(k6);
        }
        public void readFields(DataInput dataInput) throws IOException {
            k0=dataInput.readInt();
            k1=dataInput.readUTF();
            k2=dataInput.readDouble();
            k3=dataInput.readUTF();
            k4=dataInput.readUTF();
            k5=dataInput.readUTF();
            k6=dataInput.readUTF();
        }
        public Integer k0;
        public String k1;
        public Double k2;
        public String k3;
        public String k4;
        public String k5;
        public String k6;
    }
    public static class Map extends Mapper<Object,Text, CompositeKey,Text>{
        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
            String line = value.toString();
            String[] arr = Split(line, '|', 37);
            context.write(new CompositeKey(Integer.parseInt(arr[0]), arr[1], Double.parseDouble(arr[5]), arr[4], arr[9], arr[2], arr[7]), new Text(line));
        }
    }
    public static class Reduce extends Reducer<CompositeKey,Text,NullWritable,Text>{
        public void reduce(CompositeKey key,Iterable<Text> values,Context context) throws IOException,InterruptedException{
            NullWritable t = NullWritable.get();
            Double agg0 = 0.0;
            int count = 0;
            String[] arr = null;
            for (Text value : values) {
                arr = Split(value.toString(), '|', 37);
                ++count;
                agg0 += Double.parseDouble(arr[17])*(1-Double.parseDouble(arr[18]));
            }
                        context.write(t, new Text(((Object)(Integer.parseInt(arr[0]))).toString()+"|"+((Object)(arr[1])).toString()+"|"+((Object)(agg0)).toString()+"|"+((Object)(Double.parseDouble(arr[5]))).toString()+"|"+((Object)(arr[9])).toString()+"|"+((Object)(arr[2])).toString()+"|"+((Object)(arr[4])).toString()+"|"+((Object)(arr[7])).toString()));
        }
    }
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = new Job(conf,"Sort");
        job.setJarByClass(AGG1.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(CompositeKey.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path("JOIN3"));
        FileOutputFormat.setOutputPath(job, new Path("AGG1"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
