package common.schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by honghaijie on 11/19/16.
 */
public class SchemaSet {
    static SchemaSet schemaSet = new SchemaSet();
    public static SchemaSet Get() {
        return schemaSet;
    }

    private SchemaSet() {
    }
    public void Add(Schema schema) {
        schemas.put(schema.Name(), schema);
    }
    public Schema Get(String name) {
        return schemas.get(name);
    }
    private Map<String, Schema> schemas = new HashMap<>();
}
