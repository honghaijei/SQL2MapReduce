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
    public String Name() {
        return tableName;
    }
    public void Add(String name, DataType type) {
        cols.add(new SchemaColumn(name, type));
    }
    public SchemaColumn Get(int i) {
        return cols.get(i);
    }
    public int GetPosByName(String name) {
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
    public List<SchemaColumn> Cols() {
        return this.cols;
    }
    public static Schema ParseFromString(String raw) {
        // TODO
        return new Schema("");
    }
    public static Schema Combine(String name, Schema schema1, Schema schema2) {
        Schema ans = new Schema(name);
        ans.Cols().addAll(schema1.Cols());
        ans.Cols().addAll(schema2.Cols());
        return ans;
    }
    private List<SchemaColumn> cols = new ArrayList<>();
    private String tableName;
}
