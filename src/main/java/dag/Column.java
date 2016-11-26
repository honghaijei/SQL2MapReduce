package dag;

import common.schema.Schema;
import common.schema.SchemaSet;

/**
 * Created by honghaijie on 11/19/16.
 */
public class Column {
    public Column(String s, SchemaSet set) {
        String[] arr = s.split("\\.");
        key = arr[0];
        if (arr.length == 2) {
            this.table = arr[1];
        } else {
            this.table = set.GetTableNameByColumnName(key);
        }
    }
    public Column(String key, String table) {
        this.table = table;
        this.key = key;
    }
    public void SetTable(String table) {
        this.table = table;
    }
    public void SetKey(String key) {
        this.key = key;
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
