package dag;

import astree.FilterNode;
import astree.TreeNode;
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
    public abstract String GetName();
    public abstract List<Schema> GetMapperOutputSchema();
    public abstract Schema GetOutputSchema();
    public abstract List<String> GetInputs();
    public abstract String GetOutput();
    public abstract String Generate();
    public abstract void AddMapperFilter(List<TreeNode> filters);
    public abstract void AddReducerFilter(TreeNode filters);
    public abstract List<TreeNode>  GetMapperFilter();
    public abstract TreeNode GetReducerFilter();
}
