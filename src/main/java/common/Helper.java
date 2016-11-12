package common;

import common.schema.DataType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by honghaijie on 11/12/16.
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
    public static String Spaces(int n) {
        StringBuilder sb = new StringBuilder();
        while (n-- > 0) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
