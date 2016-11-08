package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/8/16.
 */
public class ArithNode extends TreeNode {
    public ArithNode() {

    }
    public String constant;
    public String columnName;
    public List<TreeNode> children = new ArrayList<>();
    public List<String> op = new ArrayList<>();
}
