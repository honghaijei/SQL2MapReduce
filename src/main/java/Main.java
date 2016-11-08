/**
 * Created by hahong on 2016/9/15.
 */
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
import org.antlr.v4.runtime.tree.ParseTree;
import tree.TreeNode;

public class Main {
    public static void main(String[] args) {
        CharStream stream = new ANTLRInputStream("select a,b from Users where a=1 or (a + b * 4) = 2  and a = 2");
        MySQLLexer lexer = new MySQLLexer(stream);
        MySQLParser parser = new MySQLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.stat();
        TreeNode answer = new StatVisitor().visit(tree);
        System.out.println(answer);
    }
}
