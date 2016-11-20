/**
 * Created by hahong on 2016/9/15.
 */
import codegen.WhereGenerator;
import common.schema.DataType;
import common.schema.Schema;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import astree.*;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CharStream stream = new ANTLRInputStream("select Users.Name, Users.Age, Users.Height, Scores.score from Users, Scores where Users.Name =  Scores.Name");
        MySQLLexer lexer = new MySQLLexer(stream);
        MySQLParser parser = new MySQLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.stat();
        SelectNode select = (SelectNode)new StatVisitor().visit(tree);
        Schema schema1 = new Schema("Users");
        schema1.Add("Name", DataType.STRING);
        schema1.Add("Age", DataType.INT32);
        schema1.Add("Height", DataType.DOUBLE);

        Schema schema2 = new Schema("Scores");
        schema2.Add("Name", DataType.STRING);
        schema2.Add("Score", DataType.INT32);

        System.out.println(new WhereGenerator(select.where, Arrays.asList(schema1, schema2)).Generate());
        System.out.println(select);
    }
}
