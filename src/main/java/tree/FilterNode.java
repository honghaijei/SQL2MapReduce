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
}
