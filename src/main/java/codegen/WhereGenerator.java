package codegen;

import common.schema.Schema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import astree.ArithNode;
import astree.FilterNode;
import astree.SimpleFilterNode;
import astree.TreeNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghaijie on 11/14/16.
 */
public class WhereGenerator {
    public WhereGenerator(TreeNode node, List<Schema> schema) {
        this.node = node;
        this.schema = schema;
    }
    public Set<String> GetTables() {
        Set<String> ans = new HashSet<>();
        GetTables(node, ans);
        return ans;
    }
    private void GetTables(TreeNode root, Set<String> tables) {
        if (root instanceof SimpleFilterNode) {
            SimpleFilterNode sfn = (SimpleFilterNode)root;
            new ArithExpressionGenerator(sfn.left, schema, null).GetTables(tables);
            new ArithExpressionGenerator(sfn.right, schema, null).GetTables(tables);
        }
        if (root instanceof FilterNode) {
            FilterNode fn = (FilterNode)root;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < fn.filters.size(); ++i) {
                GetTables(fn.filters.get(i), tables);
            }
        }
    }
    public String Generate() {
        if (node == null) return "(true)";
        return GenerateExperssion(node);
    }
    private String GenerateExperssion(TreeNode root) {
        if (root instanceof SimpleFilterNode) {
            SimpleFilterNode sfn = (SimpleFilterNode)root;
            if (sfn.op.equals("=")) {
                return GenerateExperssion(sfn.left) + ".equals(" + GenerateExperssion(sfn.right) + ")";
            }
            if (sfn.op.equals("!=")) {
                return "!" + GenerateExperssion(sfn.left) + ".equals(" + GenerateExperssion(sfn.right) + ")";
            }
            if (sfn.op.equals(">")) {
                return GenerateExperssion(sfn.left) + ".compareTo(" + GenerateExperssion(sfn.right) + ") > 0";
            }
            if (sfn.op.equals("<")) {
                return GenerateExperssion(sfn.left) + ".compareTo(" + GenerateExperssion(sfn.right) + ") < 0";
            }
            if (sfn.op.equals(">=")) {
                return GenerateExperssion(sfn.left) + ".compareTo(" + GenerateExperssion(sfn.right) + ") >= 0";
            }
            if (sfn.op.equals("<=")) {
                return GenerateExperssion(sfn.left) + ".compareTo(" + GenerateExperssion(sfn.right) + ") <= 0";
            }

            return GenerateExperssion(sfn.left) + sfn.op + GenerateExperssion(sfn.right);
        }
        if (root instanceof FilterNode) {
            FilterNode fn = (FilterNode)root;
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            if (fn.op.equals('!')) {
                sb.append("!");
            }

            for (int i = 0; i < fn.filters.size(); ++i) {
                if (i != 0 && !fn.op.equals("!")) {
                    sb.append(fn.op);
                }
                sb.append(GenerateExperssion(fn.filters.get(i)));
            }
            sb.append(')');

            return sb.toString();
        }
        if (root instanceof ArithNode) {
            ArithExpressionGenerator g = new ArithExpressionGenerator((ArithNode)root, schema, Arrays.asList("arr"));
            return g.Generate();
        }
        throw new NotImplementedException();
    }
    private TreeNode node;
    private List<Schema> schema;
}
