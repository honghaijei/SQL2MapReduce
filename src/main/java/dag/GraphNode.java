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
            schemas.add(SchemaSet.Get().Get(s));
        }
        return schemas;
    }
    abstract Schema GetOutputSchemas();
    abstract List<String> GetInputs();
    abstract String GetOutput();
    abstract String Generate();
}
