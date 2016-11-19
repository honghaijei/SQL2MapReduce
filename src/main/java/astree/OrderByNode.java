package astree;

import common.schema.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/11/16.
 */
public class OrderByNode extends TreeNode {
    public OrderByNode() {
    }
    public Schema GetInputSchema() {
        return schema;
    }
    public Schema GetOutputSchema() {
        return schema;
    }
    public String input;
    public String output;
    public Schema schema;
    public List<String> cols = new ArrayList<>();
}
