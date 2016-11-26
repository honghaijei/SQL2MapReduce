package common.schema;


/**
 * Created by honghaijie on 11/11/16.
 */
public class SchemaColumn {
    public SchemaColumn(String name, DataType type, String subTableName) {
        this.key = name;
        this.type = type;
        this.tableName = subTableName;
    }
    public boolean Match(String key) {
        String[] arr = key.split("\\.");
        if (arr.length == 1) {
            return this.key.equals(arr[0]);
        }
        return (arr[0].equals(tableName) && arr[1].equals(key));
    }

    public String key;
    public String tableName;

    public DataType type;
}
