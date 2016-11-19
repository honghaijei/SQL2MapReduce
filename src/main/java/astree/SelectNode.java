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
    private void ConvertFiltersToJoinNode() {
        List<SimpleFilterNode> nodes = FindAndRemoveEquiJoin();
        int cnt = 0;
        Map<String, TableNode> t = new HashMap<>();
        for (SimpleFilterNode node : nodes) {
            String columnName1 = ((ArithNode)node.left).columnName;
            String columnName2 = ((ArithNode)node.right).columnName;
            String table1 = columnName1.split("\\.")[0];
            String table2 = columnName2.split("\\.")[0];
            String key1 = columnName1.split("\\.")[1];
            String key2 = columnName2.split("\\.")[1];
            TableNode node1 = null;
            TableNode node2 = null;
            if (t.containsKey(key1)) {
                node1 = t.get(key1);
            } else {
                node1 = FindTableByNameOrAlias(table1);
            }
            if (t.containsKey(key2)) {
                node2 = t.get(key2);
            } else {
                node2 = FindTableByNameOrAlias(table2);
            }
            JoinNode Node = new JoinNode(key1, key2, node1, node2);

        }
    }
    private List<SimpleFilterNode> FindAndRemoveEquiJoin() {
        List<SimpleFilterNode> ans = new ArrayList<>();
        if (where instanceof SimpleFilterNode) {
            if (((SimpleFilterNode) where).IsEqui()) {
                ans.add((SimpleFilterNode) where);
                where = null;
            }
            return ans;
        } else {
            FilterNode fnode = (FilterNode)where;

            if (!fnode.IsAnd()) {
                return ans;
            }
            UnionFindSet s = new UnionFindSet();
            List<TreeNode> newFilters = new ArrayList<>();
            for (TreeNode ch : fnode.filters) {
                if (!(ch instanceof SimpleFilterNode)) {
                    continue;
                }
                SimpleFilterNode snode = (SimpleFilterNode) ch;
                if (!snode.IsEqui()) {
                    continue;
                }
                ArithNode left = (ArithNode) snode.left;
                ArithNode right = (ArithNode) snode.right;
                if (left.IsColumn() && right.IsColumn()) {
                    String leftTable = left.columnName.split("\\.")[0];
                    String rightTable = right.columnName.split("\\.")[0];
                    boolean succ = s.Add(leftTable, rightTable);
                    if (succ) {
                        ans.add(snode);
                    } else {
                        newFilters.add(ch);
                    }
                } else {
                    newFilters.add(ch);
                }
            }
            fnode.filters = newFilters;
            return ans;
        }
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
