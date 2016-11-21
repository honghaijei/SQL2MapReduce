package common.schema;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by honghaijie on 11/19/16.
 */
public class SchemaSet {
    static SchemaSet schemaSet = new SchemaSet();
    public static SchemaSet Instance() {
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
    public String GetTableNameByColumnName(String s) {
        String[] arr = s.split("\\.");
        if (arr.length == 2) {
            return arr[0];
        }
        for (String k : schemas.keySet()) {
            Schema sc = schemas.get(k);
            if (sc.GetPosByName(arr[0]) != -1) {
                return k;
            }
        }
        return null;
    }

    private Map<String, Schema> schemas = new LinkedHashMap<>();
}
