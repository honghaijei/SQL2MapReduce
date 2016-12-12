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
    public static String JoinFunction = "    public static String Join(String sep, String[] arr) {\n" +
            "        StringBuilder sb = new StringBuilder();\n" +
            "        for (int i = 0; i < arr.length; ++i) {\n" +
            "            if (i != 0) {\n" +
            "                sb.append(sep);\n" +
            "            }\n" +
            "            sb.append(arr[i]);\n" +
            "        }\n" +
            "        return sb.toString();\n" +
            "    }";
    public static String OrderPreserveEncoder = "import java.nio.ByteBuffer;\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class OrderPreserveEncoder {\n" +
            "    public OrderPreserveEncoder() {\n" +
            "    }\n" +
            "    private void appendByteArray(byte[] arr) {\n" +
            "        for (byte b : arr) {\n" +
            "            encode.add(b);\n" +
            "        }\n" +
            "    }\n" +
            "    public void EncodeString(String s) {\n" +
            "        try {\n" +
            "            appendByteArray(s.getBytes(\"utf-8\"));\n" +
            "            encode.add((byte) 0);\n" +
            "        } catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "        flag = flag * 4 + 1;\n" +
            "    }\n" +
            "    public void EncodeInteger(int i) {\n" +
            "        long r = i;\n" +
            "        r -= Integer.MIN_VALUE;\n" +
            "        byte[] bytes = ByteBuffer.allocate(8).putLong(r).array();\n" +
            "        for (int p = 4; p < 8; ++p) {\n" +
            "            encode.add(bytes[p]);\n" +
            "        }\n" +
            "        flag = flag * 4 + 2;\n" +
            "    }\n" +
            "    public void EncodeDouble(double d) {\n" +
            "        long lng = Double.doubleToLongBits(d);\n" +
            "        lng ^= (1l << 63);\n" +
            "        byte[] bytes = ByteBuffer.allocate(8).putLong(lng).array();\n" +
            "        appendByteArray(bytes);\n" +
            "        flag = flag * 4 + 3;\n" +
            "    }\n" +
            "    public byte[] GetEncodedByteArray() {\n" +
            "        byte[] bytes = ByteBuffer.allocate(8).putLong(flag).array();\n" +
            "        appendByteArray(bytes);\n" +
            "        byte[] ans = new byte[encode.size()];\n" +
            "        for (int i = 0; i < ans.length; ++i) {\n" +
            "            ans[i] = encode.get(i);\n" +
            "        }\n" +
            "        return ans;\n" +
            "    }\n" +
            "    private int flag = 0;\n" +
            "    private List<Byte> encode = new ArrayList<Byte>();\n" +
            "\n" +
            "    public static int compare(byte[] a, byte[] b) {\n" +
            "        for (int i = 0; i < a.length; ++i) {\n" +
            "            if (i >= b.length) {\n" +
            "                return 1;\n" +
            "            }\n" +
            "            int av = (int) a[i] & 0xff;\n" +
            "            int bv = (int) b[i] & 0xff;\n" +
            "            if (av > bv) {\n" +
            "                return 1;\n" +
            "            } else if (av < bv) {\n" +
            "                return -1;\n" +
            "            }\n" +
            "        }\n" +
            "        return a.length == b.length ? 0 : -1;\n" +
            "    }\n" +
            "}\n";
}
