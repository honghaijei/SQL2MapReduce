package codegen;

/**
 * Created by honghaijie on 11/11/16.
 */
public class Helper {
    String Import = "import java.io.IOException;\n" +
            "import java.util.*;\n" +
            "import java.text.*;\n" +
            "import org.apache.hadoop.fs.Path;\n" +
            "import org.apache.hadoop.conf.*;\n" +
            "import org.apache.hadoop.io.*;\n" +
            "import org.apache.hadoop.util.Tool;\n" +
            "import org.apache.hadoop.util.ToolRunner;\n" +
            "import org.apache.hadoop.mapreduce.Job;\n" +
            "import org.apache.hadoop.mapreduce.Mapper;\n" +
            "import org.apache.hadoop.mapreduce.Reducer;\n" +
            "import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;\n" +
            "import org.apache.hadoop.mapreduce.lib.input.FileSplit;\n" +
            "import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;\n" +
            "import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;\n" +
            "import org.apache.hadoop.mapreduce.lib.partition.*;";

    String SplitFunction = "        public String[] Split(String s, char seperator, int expected_length) {\n" +
            "            String[] ans = new String[expected_length];\n" +
            "            int pos = 0;\n" +
            "            for(int i = 0;;) {\n" +
            "                int nx = s.indexOf(seperator, pos);\n" +
            "                if (nx == -1) nx = s.length();\n" +
            "                ans[i++] = s.substring(pos, nx);\n" +
            "                if (nx == s.length()) break;\n" +
            "                pos = nx + 1;\n" +
            "            };\n" +
            "            return ans;\n" +
            "        }";
}
