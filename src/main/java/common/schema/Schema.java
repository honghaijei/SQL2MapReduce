package common.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/11/16.
 */
public class Schema {
    public Schema(String tableName) {
        this.tableName = tableName;
    }
    public void Add(String name, DataType type) {
        cols.add(new SchemaColumn(name, type));
    }
    public SchemaColumn Get(int i) {
        return cols.get(i);
    }
    public int FindByName(String name) {
        String[] arr = name.split("\\.");
        String qtable = null;
        String qcol = null;
        if (arr.length > 1) {
            qtable = arr[0];
            qcol = arr[1];
        } else {
            qcol = arr[0];
        }
        if (qtable != null && !qtable.equals(this.tableName)) {
            return -1;
        }
        int ret = 0;
        for (SchemaColumn sc : cols) {
            if (sc.name.equals(qcol)) {
                return ret;
            }
            ++ret;
        }
        return -1;
    }
    public int size() {
        return cols.size();
    }
    public static Schema ParseFromString(String raw) {
        // TODO
        return new Schema("");
    }
    private List<SchemaColumn> cols = new ArrayList<>();
    private String tableName;
}
