package common.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/11/16.
 */
public class Schema {
    public Schema() {

    }
    public void Add(String name, DataType type) {
        cols.add(new SchemaColumn(name, type));
    }
    public SchemaColumn Get(int i) {
        return cols.get(i);
    }
    public int FindByName(String name) {
        int ret = 0;
        for (SchemaColumn sc : cols) {
            if (sc.name == name) {
                return ret;
            }
            ++ret;
        }
        return -1;
    }
    public static Schema ParseFromString(String raw) {
        // TODO
        return new Schema();
    }
    private List<SchemaColumn> cols = new ArrayList<>();
}
