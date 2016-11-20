package dag;

import common.schema.Schema;
import common.schema.SchemaSet;

/**
 * Created by honghaijie on 11/19/16.
 */
public class Column {
    public Column(String s) {
        String[] arr = s.split("\\.");
        if (arr.length == 1) {
            key = arr[0];
            table = SchemaSet.Instance().GetTableNameByColumnName(key);
        } else {
            table = arr[0];
            key = arr[1];
        }
    }
    public Column(String s, String table) {
        String[] arr = s.split("\\.");
        if (arr.length == 1) {
            key = arr[0];
            this.table = table;
        } else {
            this.table = arr[0];
            key = arr[1];
        }
    }
    public String GetTable() {
        return table;
    }
    public String GetKey() {
        return key;
    }
    private String table;
    private String key;
}
