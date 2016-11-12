package common.schema;


/**
 * Created by honghaijie on 11/11/16.
 */
public class SchemaColumn {
    public SchemaColumn(String name, DataType type) {
        this.name = name;
        this.type = type;
    }
    public String name;
    public DataType type;
}
