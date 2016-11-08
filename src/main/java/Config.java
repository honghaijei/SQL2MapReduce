/**
 * Created by hahong on 2016/9/16.
 */
public class Config {
    public static String MapReduceHead = "import java.io.IOException;\n" +
            "import java.util.Iterator;\n" +
            "import java.util.StringTokenizer;\n" +
            "import org.apache.hadoop.fs.Path;\n" +
            "import org.apache.hadoop.io.IntWritable;\n" +
            "import org.apache.hadoop.io.LongWritable;\n" +
            "import org.apache.hadoop.io.Text;\n" +
            "import org.apache.hadoop.mapred.FileInputFormat;\n" +
            "import org.apache.hadoop.mapred.FileOutputFormat;\n" +
            "import org.apache.hadoop.mapred.JobClient;\n" +
            "import org.apache.hadoop.mapred.JobConf;\n" +
            "import org.apache.hadoop.mapred.MapReduceBase;\n" +
            "import org.apache.hadoop.mapred.Mapper;\n" +
            "import org.apache.hadoop.mapred.OutputCollector;\n" +
            "import org.apache.hadoop.mapred.Reducer;\n" +
            "import org.apache.hadoop.mapred.Reporter;\n" +
            "import org.apache.hadoop.mapred.TextInputFormat;\n" +
            "import org.apache.hadoop.mapred.TextOutputFormat;\n\n";
    public static String MapperHead = "public class WordCount {\n" +
            "   public static class Map extends MapReduceBase implements\n" +
            "       Mapper<LongWritable, Text, Text, IntWritable> {";
}
