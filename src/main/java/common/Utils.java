package common;

import common.schema.Schema;
import dag.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by honghaijie on 11/12/16.
 */
public class Utils {
    private static Map<String, Integer> TaskName = new HashMap<>();
    public static String GetTaskName(String s) {
        if (!TaskName.containsKey(s)) {
            TaskName.put(s, 0);
        }
        TaskName.put(s, TaskName.get(s) + 1);
        return s + TaskName.get(s);
    }
    public static int GetSchemaIdxByColumnName(List<Schema> schemas, String columnName) {
        for (int i = 0; i < schemas.size(); ++i) {
            Schema schema = schemas.get(i);
            Column c = new Column(columnName);
            if (c.GetTable() != null && !c.GetTable().equals(schema.Name())) {
                continue;
            }
            int idx = schema.GetPosByName(columnName);
            if (idx == -1) {
                continue;
            }
            return i;
        }
        return -1;
    }
}
