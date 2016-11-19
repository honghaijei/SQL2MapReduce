package common;

import org.apache.hadoop.fs.shell.find.Find;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by honghaijie on 11/15/16.
 */
public class UnionFindSet {
    class Node {
        Node parent;
    }
    public UnionFindSet() {
    }
    public boolean Add(String left, String right) {
        Node lnode = CreateIfNotExist(left);
        Node rnode = CreateIfNotExist(right);
        Node lroot = FindRoot(lnode);
        Node rroot = FindRoot(rnode);
        if (lroot == rroot) {
            return false;
        }
        lroot.parent = rroot;
        return true;
    }
    public Node FindRoot(Node node) {
        if (node.parent == node) {
            return node;
        }
        Node ans = FindRoot(node.parent);
        node.parent = ans;
        return ans;
    }
    public Node CreateIfNotExist(String s) {
        if (mapper.get(s) != null) {
            return mapper.get(s);
        }
        Node ans = new Node();
        ans.parent = ans;
        mapper.put(s, ans);
        return ans;
    }
    Map<String, Node> mapper = new HashMap<>();
}
