/**
 * Created by hahong on 2016/9/15.
 */
import common.Utils;
import common.schema.DataType;
import common.schema.Schema;
import common.schema.SchemaSet;
import dag.GraphNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import astree.*;

import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        String sqlStr = "select\n" +
                "\tc_custkey,\n" +
                "\tc_name,\n" +
                "\tsum(l_extendedprice * (1 - l_discount)) as revenue,\n" +
                "\tc_acctbal,\n" +
                "\tn_name,\n" +
                "\tc_address,\n" +
                "\tc_phone,\n" +
                "\tc_comment\n" +
                "from\n" +
                "\tcustomer,\n" +
                "\torders,\n" +
                "\tlineitem,\n" +
                "\tnation\n" +
                "where\n" +
                "\tc_nationkey = n_nationkey\n" +
                "\tand c_custkey = o_custkey\n" +
                "\tand l_orderkey = o_orderkey\n" +
                "\tand o_orderdate >= '1993-05-01'\n" +
                "\tand o_orderdate < '1993-08-01'\n" +
                "\tand l_returnflag = 'R'\n" +
                "group by\n" +
                "\tc_custkey,\n" +
                "\tc_name,\n" +
                "\tc_acctbal,\n" +
                "\tc_phone,\n" +
                "\tn_name,\n" +
                "\tc_address,\n" +
                "\tc_comment\n" +
                "order by\n" +
                "\trevenue desc;\n";
        //sqlStr = sqlStr.replace('\t', ' ').replace('\n', ' ');
        CharStream stream = new ANTLRInputStream(sqlStr);
        MySQLLexer lexer = new MySQLLexer(stream);
        MySQLParser parser = new MySQLParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.stat();
        SelectNode select = (SelectNode)new StatVisitor().visit(tree);

        String outputPath = "codes";
        Utils.CreateFolderIfNotExist(outputPath);
        DagConverter c = new DagConverter();
        c.Convert(select);
        int i = 1;
        for (GraphNode node : c.GetNodes()) {
            try(PrintWriter out = new PrintWriter(outputPath + "/" + node.GetName() + ".java")){
                out.println(node.Generate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(select);
    }
}
