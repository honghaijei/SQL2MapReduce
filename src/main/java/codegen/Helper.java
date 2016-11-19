package codegen;

import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.crypto.Data;

/**
 * Created by honghaijie on 11/11/16.
 */
public class Helper {
    public static String DataType2JavaType(DataType type) {
        if (type == DataType.INT32) {
            return "Integer";
        }
        if (type == DataType.DOUBLE) {
            return "Double";
        }
        if (type == DataType.STRING) {
            return "String";
        }
        throw new NotImplementedException();
    }
    public static String DefaultValue(DataType type) {
        if (type == DataType.DOUBLE) {
            return "0.0";
        }
        if (type == DataType.INT32) {
            return "0";
        }
        if (type == DataType.STRING) {
            return "";
        }
        throw new NotImplementedException();
    }
    public static String DataType2MapReduceType(DataType type) {
        if (type == DataType.INT32) {
            return "IntWritable";
        }
        if (type == DataType.DOUBLE) {
            return "DoubleWritable";
        }
        if (type == DataType.STRING) {
            return "Text";
        }
        throw new NotImplementedException();
    }
    public static String ParseString2JavaType(String var, DataType type) {
        if (type == DataType.INT32) {
            return "Integer.parseInt(" + var + ")";
        }
        if (type == DataType.DOUBLE) {
            return "Double.parseDouble(" + var + ")";
        }
        if (type == DataType.STRING) {
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
            "\n" +
            "import org.apache.hadoop.fs.Path;\n" +
            "import org.apache.hadoop.conf.*;\n" +
            "import org.apache.hadoop.io.*;\n" +
            "import org.apache.hadoop.mapreduce.Job;\n" +
            "import org.apache.hadoop.mapreduce.Mapper;\n" +
            "import org.apache.hadoop.mapreduce.Reducer;\n" +
            "import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;\n" +
            "import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;\n";

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
