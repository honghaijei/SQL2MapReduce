package codegen;

import common.schema.DataType;
import common.schema.Schema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import astree.ArithNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static common.Utils.GetSchemaIdxByColumnName;

/**
 * Created by honghaijie on 11/12/16.
 */
public class ArithExpressionGenerator {
    public ArithExpressionGenerator(ArithNode node, List<Schema> schemas, List<String> arrayNames) {
        this.node = node;
        this.schemas = schemas;
        this.arrayNames = arrayNames;
        node.GuessReturnType();
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
    public void GetTables(Set<String> set) {
        GetTables(node, set);
    }
    private void GetTables(ArithNode node, Set<String> set) {
        if (node.constant != null) {
            return;
        }
        if (node.columnName != null) {
            int schemaIdx = GetSchemaIdxByColumnName(schemas, node.columnName);
            set.add(schemas.get(schemaIdx).Name());
        }

        for (int i = 0; i < node.children.size(); ++i) {
            ArithNode ch = node.children.get(i);
            GetTables(ch, set);
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
            int schemaIdx = GetSchemaIdxByColumnName(schemas, node.columnName);
            int idx = schemas.get(schemaIdx).GetPosByName(node.columnName);
            return Helper.ParseString2JavaType(arrayNames.get(schemaIdx) + "["+idx + "]", node.type);
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
    private List<Schema> schemas;
    private List<String> arrayNames;
}
