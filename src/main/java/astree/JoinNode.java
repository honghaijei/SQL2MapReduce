package astree;

/**
 * Created by honghaijie on 11/15/16.
 */
public class JoinNode {
    public JoinNode(String key1, String key2, TableNode node1, TableNode node2) {
        this.key1 = key1;
        this.key2 = key2;
        this.node1 = node1;
        this.node2 = node2;
    }
    public String key1;
    public String key2;
    public TableNode node1;
    public TableNode node2;
}
