package astree;

/**
 * Created by honghaijie on 11/8/16.
 */
public class SimpleFilterNode extends TreeNode {
    public SimpleFilterNode() {

    }
    public boolean IsEqui() {
        return op.equals("=");
    }
    public ArithNode left;
    public ArithNode right;
    public String op;
}
