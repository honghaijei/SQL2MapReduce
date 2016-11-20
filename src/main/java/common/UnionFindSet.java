package common;

import org.apache.hadoop.fs.shell.find.Find;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by honghaijie on 11/15/16.
 */
public class UnionFindSet<T> {
    class Node {
        Node parent;
        T val;
    }
    public UnionFindSet() {
    }
    public boolean Add(T root, T joinnode) {
        Node lnode = CreateIfNotExist(root);
        Node rnode = CreateIfNotExist(joinnode);
        Node lroot = FindRoot(lnode);
        Node rroot = FindRoot(rnode);
        if (lroot == rroot) {
            return false;
        }
        rroot.parent = lroot;
        return true;
    }
    public boolean HasSameRoot(T l, T r) {
        Node lnode = mapper.get(l);
        Node rnode = mapper.get(r);
        if (lnode == null || rnode == null) {
            return false;
        }
        Node lroot = FindRoot(lnode);
        Node rroot = FindRoot(rnode);
        return lroot == rroot;
    }
    public T FindRoot(T val) {
        return FindRoot(CreateIfNotExist(val)).val;
    }
    private Node FindRoot(Node node) {
        if (node.parent == node) {
            return node;
        }
        Node ans = FindRoot(node.parent);
        node.parent = ans;
        return ans;
    }
    public Node CreateIfNotExist(T s) {
        if (mapper.get(s) != null) {
            return mapper.get(s);
        }
        Node ans = new Node();
        ans.parent = ans;
        ans.val = s;
        mapper.put(s, ans);
        return ans;
    }
    Map<T, Node> mapper = new HashMap<>();
}
