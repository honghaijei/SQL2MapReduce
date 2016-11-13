package codegen;

import common.schema.DataType;
import common.schema.Schema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tree.ArithNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by honghaijie on 11/12/16.
 */
public class ArithExpressionGenerator {
    public ArithExpressionGenerator(ArithNode node, Schema schema) {
        this.node = node;
        this.schema = schema;
    }
    public List<ArithNode> GetAggrInternal() {
        List<ArithNode> ans = new ArrayList<>();
        SearchAggrFunction(node, ans);
        return ans;
    }
    void SearchAggrFunction(ArithNode cur, List<ArithNode> nodes) {
        if (cur.function != null) {
            nodes.add(cur);
            return;
        }
        for (ArithNode ch : cur.children) {
            SearchAggrFunction(ch, nodes);
        }
    }
    public String Generate() {
        return Generate(node);
    }
    public DataType ReturnType() {
        return node.type;
    }
    public String Generate(ArithNode node) {
        if (node.constant != null) {
            return node.constant;
        }
        if (node.columnName != null) {
            int idx = schema.FindByName(node.columnName);
            return Helper.ParseString2JavaType("arr["+idx + "]", node.type);
        }
        if (node.function != null) {
            throw new NotImplementedException();
        }
        StringBuilder sb = new StringBuilder();
        boolean needParentheses = true;
        if (node.children.size() <= 1 || node.op.get(0).equals("*") || node.op.get(0).equals("/")) {
            needParentheses = false;
        }
        if (needParentheses) {
            sb.append("(");
        }

        for (int i = 0; i < node.children.size(); ++i) {
            ArithNode ch = node.children.get(i);
            if (i != 0) {
                sb.append(node.op.get(i - 1));
            }
            sb.append(Generate(ch));
        }
        if (needParentheses) {
            sb.append(")");
        }
        return sb.toString();
    }
    private ArithNode node;
    private Schema schema;
}