package common.schema;

import common.Constant;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by honghaijie on 11/19/16.
 */
public class SchemaSet {
    static SchemaSet schemaSet = ParseFromFile(Constant.SchemaFileLocation);
    public static SchemaSet Instance() {
        return schemaSet;
    }

    private SchemaSet() {
    }
    public SchemaSet SubSet(List<String> tables) {
        SchemaSet ans = new SchemaSet();
        for (String table : tables) {
            ans.schemas.put(table, schemas.get(table));
        }
        return ans;
    }
    private static SchemaSet ParseFromFile(String file) {
        SchemaSet ans = new SchemaSet();
        try {
            List<String> lines = Files.readAllLines(Paths.get(file), Charset.forName("utf-8"));
            for (String line : lines) {
                ans.Add(Schema.ParseFromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
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
