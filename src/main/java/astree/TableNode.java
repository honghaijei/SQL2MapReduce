package astree;

/**
 * Created by honghaijie on 11/8/16.
 */
public class TableNode extends TreeNode {
    public TableNode() {
    }
    public SelectNode subQuery;
    public String tableName;
    public String alias;
}