package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/8/16.
 */
public class SelectNode extends TreeNode {
    public SelectNode() {

    }
    public List<TreeNode> from = new ArrayList<>();
    public List<String> columns = new ArrayList<>();
    public TreeNode where;
    public GroupByNode groupby;
    public OrderByNode orderby;
}
