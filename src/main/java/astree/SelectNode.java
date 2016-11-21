package astree;

import common.UnionFindSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by honghaijie on 11/8/16.
 */
public class SelectNode extends TreeNode {
    public SelectNode() {

    }
    private TableNode FindTableByNameOrAlias(String name) {
        for (TableNode node : from) {
            if (node.alias.equals(name) || node.tableName.equals(name)) {
                return node;
            }
        }
        return null;
    }

    public List<TableNode> from = new ArrayList<>();
    public List<ArithNode> columns = new ArrayList<>();
    //FilterNode or SimpleFilterNode
    public TreeNode where;

    public GroupByNode groupby;
    public OrderByNode orderby;
}
