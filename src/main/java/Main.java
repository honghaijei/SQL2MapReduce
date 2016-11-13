/**
 * Created by hahong on 2016/9/15.
 */
import codegen.AggrGenerator;
import codegen.ArithExpressionGenerator;
import common.schema.DataType;
import common.schema.Schema;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
import org.antlr.v4.runtime.tree.ParseTree;
import tree.FilterNode;
import tree.SelectNode;
import tree.TreeNode;

public class Main {
    public static void main(String[] args) {
        CharStream stream = new ANTLRInputStream("select avg(Age + 1) / 7.0 from Users group by Name");
        MySQLLexer lexer = new MySQLLexer(stream);
        MySQLParser parser = new MySQLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.stat();
        SelectNode select = (SelectNode)new StatVisitor().visit(tree);
        Schema schema = new Schema();
        schema.Add("Name", DataType.STRING);
        schema.Add("Age", DataType.INT32);
        schema.Add("Height", DataType.DOUBLE);
        select.columns.get(0).GuessReturnType(schema);
        System.out.println(new AggrGenerator(select, schema, "input", "output").Generate());
        System.out.println(select);
    }
}
