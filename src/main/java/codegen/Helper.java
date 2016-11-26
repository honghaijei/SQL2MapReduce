package codegen;

import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by honghaijie on 11/11/16.
 */
public class Helper {
    public static String DataType2JavaType(DataType type) {
        if (type == DataType.INTEGER) {
            return "Integer";
        }
        if (type == DataType.DECIMAL) {
            return "Double";
        }
        if (type == DataType.TEXT) {
            return "String";
        }
        throw new NotImplementedException();
    }
    public static String DefaultValue(DataType type) {
        if (type == DataType.DECIMAL) {
            return "0.0";
        }
        if (type == DataType.INTEGER) {
            return "0";
        }
        if (type == DataType.TEXT) {
            return "";
        }
        throw new NotImplementedException();
    }
    public static String DataType2MapReduceType(DataType type) {
        if (type == DataType.INTEGER) {
            return "IntWritable";
        }
        if (type == DataType.DECIMAL) {
            return "DoubleWritable";
        }
        if (type == DataType.TEXT) {
            return "Text";
        }
        throw new NotImplementedException();
    }
    public static String ParseString2JavaType(String var, DataType type) {
        if (type == DataType.INTEGER) {
            return "Integer.parseInt(" + var + ")";
        }
        if (type == DataType.DECIMAL) {
            return "Double.parseDouble(" + var + ")";
        }
        if (type == DataType.TEXT) {
            return var;
        }
        throw new NotImplementedException();
    }
    public static String Spaces(int n) {
        StringBuilder sb = new StringBuilder();
        while (n-- > 0) {
            sb.append(' ');
        }
        return sb.toString();
    }
    public static String Import = "import java.io.DataInput;\n" +
            "import java.io.DataOutput;\n" +
            "import java.io.IOException;\n" +
            "import org.apache.hadoop.fs.Path;\n" +
            "import org.apache.hadoop.conf.*;\n" +
            "import org.apache.hadoop.io.*;\n" +
            "import org.apache.hadoop.mapreduce.Job;\n" +
            "import org.apache.hadoop.mapreduce.Mapper;\n" +
            "import org.apache.hadoop.mapreduce.Reducer;\n" +
            "import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;\n" +
            "import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;\n" +
            "import java.util.ArrayList;\n"+
            "import java.util.List;\n"+
            "import org.apache.hadoop.mapreduce.lib.input.FileSplit;\n" +
            "\n";

    public static String SplitFunction = "    public static String[] Split(String s, char seperator, int expected_length) {\n" +
            "        String[] code = new String[expected_length];\n" +
            "        int pos = 0;\n" +
            "        for(int i = 0;;) {\n" +
            "            int nx = s.indexOf(seperator, pos);\n" +
            "            if (nx == -1 || i == expected_length - 1) nx = s.length();\n" +
            "            code[i++] = s.substring(pos, nx);\n" +
            "            if (nx == s.length()) break;\n" +
            "            pos = nx + 1;\n" +
            "        };\n" +
            "        return code;\n" +
            "    }";
}
