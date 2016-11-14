package codegen;

import common.schema.Schema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tree.ArithNode;
import tree.FilterNode;
import tree.SimpleFilterNode;
import tree.TreeNode;

/**
 * Created by honghaijie on 11/14/16.
 */
public class WhereGenerator {
    public WhereGenerator(FilterNode node, Schema schema) {
        this.node = node;
        this.schema = schema;
    }
    public String Generate() {
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

            return GenerateExperssion(sfn.left) + sfn.op + GenerateExperssion(sfn.right);
        }
        if (root instanceof FilterNode) {
            FilterNode fn = (FilterNode)root;
            StringBuilder sb = new StringBuilder();
            if (fn.op.equals("||")) {
                sb.append('(');
            }
            for (int i = 0; i < fn.filters.size(); ++i) {
                if (i != 0) {
                    sb.append(fn.op);
                }
                sb.append(GenerateExperssion(fn.filters.get(i)));
            }
            if (fn.op.equals("||")) {
                sb.append(')');
            }
            return sb.toString();
        }
        if (root instanceof ArithNode) {
            ArithExpressionGenerator g = new ArithExpressionGenerator((ArithNode)root, schema);
            return g.Generate();
        }
        throw new NotImplementedException();
    }
    private FilterNode node;
    private Schema schema;
}
