package common;

import astree.FilterNode;
import astree.SimpleFilterNode;
import astree.TreeNode;
import common.schema.Schema;
import dag.Column;

import java.io.File;
import java.util.*;

/**
 * Created by honghaijie on 11/12/16.
 */
public class Utils {
    private static Map<String, Integer> TaskName = new HashMap<>();
    public static String GetTaskName(String s) {
        if (!TaskName.containsKey(s)) {
            TaskName.put(s, 0);
        }
        TaskName.put(s, TaskName.get(s) + 1);
        return s + TaskName.get(s);
    }
    public static int GetSchemaIdxByColumnName(List<Schema> schemas, String columnName) {
        for (int i = 0; i < schemas.size(); ++i) {
            Schema schema = schemas.get(i);
            int idx = schema.GetPosByName(columnName);
            if (idx == -1) {
                continue;
            }
            return i;
        }
        return -1;
    }
    public static void CreateFolderIfNotExist(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            try{
                theDir.mkdir();
            }
            catch(SecurityException se){
                //handle it
            }
        }
    }
    private static void SimplifyFilterNodeAnd(TreeNode node, List<TreeNode> nodes) {
        if (node instanceof SimpleFilterNode) {
            nodes.add(node);
        }
        if (node instanceof FilterNode) {
            FilterNode fnode = (FilterNode) node;
            if (fnode.IsAnd()) {
                for (TreeNode ch : fnode.filters) {
                    SimplifyFilterNodeAnd(ch, nodes);
                }
            } else {
                nodes.add(node);
            }
        }
    }
    public static TreeNode SimplifyFilterNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        List<TreeNode> fs = new ArrayList<>();
        SimplifyFilterNodeAnd(node, fs);
        if (fs.size() == 1) {
            return fs.get(0);
        }
        FilterNode ans = new FilterNode();
        for (TreeNode t : fs) {
            ans.filters.add(t);
        }
        ans.op = "&&";
        return ans;
    }
    public static TreeNode And(TreeNode left, TreeNode right) {
        return And(Arrays.asList(left, right));
    }
    public static TreeNode And(List<TreeNode> nodes) {
        if (nodes == null || nodes.isEmpty()) return null;
        FilterNode ans = new FilterNode();
        ans.op = "&&";
        for (TreeNode node : nodes) {
            if (node == null) continue;
            ans.filters.add(node);
        }
        if (ans.filters.size() == 1) {
            return ans.filters.get(0);
        }
        return ans;
    }
}
