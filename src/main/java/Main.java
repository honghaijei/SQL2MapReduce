/**
 * Created by hahong on 2016/9/15.
 */
import codegen.WhereGenerator;
import common.Utils;
import common.schema.DataType;
import common.schema.Schema;
import common.schema.SchemaSet;
import dag.GraphNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import astree.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
                "\tc_custkey = o_custkey\n" +
                "\tand l_orderkey = o_orderkey\n" +
                "\tand o_orderdate >= '1993-05-01'\n" +
                "\tand o_orderdate < '1993-08-01'\n" +
                "\tand l_returnflag = 'R'\n" +
                "\tand c_nationkey = n_nationkey\n" +
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
        Schema schema1 = new Schema("customer");
        schema1.Add("c_custkey", DataType.STRING);
        schema1.Add("c_name", DataType.STRING);
        schema1.Add("c_acctbal", DataType.STRING);
        schema1.Add("c_address", DataType.STRING);
        schema1.Add("c_phone", DataType.STRING);
        schema1.Add("c_comment", DataType.STRING);
        schema1.Add("c_nationkey", DataType.STRING);


        Schema schema2 = new Schema("orders");
        schema2.Add("o_custkey", DataType.STRING);
        schema2.Add("o_orderdate", DataType.STRING);
        schema2.Add("o_orderkey", DataType.STRING);

        Schema schema3 = new Schema("lineitem");
        schema3.Add("l_orderkey", DataType.STRING);
        schema3.Add("l_returnflag", DataType.STRING);
        schema3.Add("l_extendedprice", DataType.DOUBLE);
        schema3.Add("l_discount", DataType.DOUBLE);

        Schema schema4 = new Schema("nation");
        schema4.Add("n_nationkey", DataType.STRING);
        schema4.Add("n_name", DataType.STRING);

        SchemaSet.Instance().Add(schema1);
        SchemaSet.Instance().Add(schema2);
        SchemaSet.Instance().Add(schema3);
        SchemaSet.Instance().Add(schema4);

        String outputPath = "codes";
        Utils.CreateFolderIfNotExist(outputPath);
        DagConverter c = new DagConverter();
        c.Convert(select);
        for (GraphNode node : c.GetNodes()) {
            try(PrintWriter out = new PrintWriter(outputPath + "/" + node.GetOutput())){
                out.println(node.Generate());
            } catch (Exception e) {

            }
        }

        System.out.println(select);
    }
}
