package dag;

import common.schema.Schema;
import common.schema.SchemaSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/19/16.
 */
public abstract class GraphNode {
    public List<Schema> GetInputSchemas() {
        List<Schema> schemas = new ArrayList<>();
        for (String s : GetInputs()) {
            schemas.add(SchemaSet.Instance().Get(s));
        }
        return schemas;
    }
    public abstract Schema GetOutputSchemas();
    public abstract List<String> GetInputs();
    public abstract String GetOutput();
    public abstract String Generate();
}
