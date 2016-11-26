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
    public void Add(String name, DataType type, String tableName) {
        cols.add(new SchemaColumn(name, type, tableName));
    }
    public SchemaColumn Get(int i) {
        return cols.get(i);
    }
    public int GetPosByName(String name) {
        int ret = 0;
        for (SchemaColumn sc : cols) {
            if (sc.Match(name)) {
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
        String[] p = raw.split("\\|");
        String tableName = p[0].toLowerCase();
        Schema res = new Schema(tableName);
        for (int i = 1; i < p.length; ++i) {
            String[] c = p[i].split(":");
            res.Add(c[0].toLowerCase(), DataType.valueOf(c[1]), tableName);
        }
        return res;
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
