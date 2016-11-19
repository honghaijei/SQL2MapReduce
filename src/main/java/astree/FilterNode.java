package astree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/8/16.
 */
public class FilterNode extends TreeNode {
    public FilterNode() {
    }
    public boolean IsAnd() {
        return op.equals("&&");
    }
    public boolean IsOr() {
        return op.equals("||");
    }
    public boolean IsNot() {
        return op.equals("!");
    }
    public List<TreeNode> filters = new ArrayList<>();
    public String op;


}
