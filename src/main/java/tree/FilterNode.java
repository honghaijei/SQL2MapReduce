package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/8/16.
 */
public class FilterNode extends TreeNode {
    public FilterNode() {
    }

    public List<TreeNode> filters = new ArrayList<>();
    public String op;

    public List<FilterNode> FindEquiJoin() {
        List<FilterNode> ans = new ArrayList<>();
        if (!this.op.equals("&&")) {
            return ans;
        }
        for (TreeNode ch : filters) {
            if (!(ch instanceof SimpleFilterNode)) {
                continue;
            }
            SimpleFilterNode snode = (SimpleFilterNode)ch;
            if (!snode.op.equals("=")) {
                continue;
            }

        }
        return ans;
    }
}
