import common.schema.DataType;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.lang.NotImplementedException;
import astree.*;

/**
 * Created by hahong on 2016/9/16.
 */
public class StatVisitor extends MySQLParserBaseVisitor<TreeNode> {

    @Override
    public TreeNode visitSelect_clause(@NotNull MySQLParser.Select_clauseContext ctx) {
        SelectNode node = new SelectNode();
        if (ctx.where_clause() != null) {
            node.where = visit(ctx.where_clause());
        }
        for (MySQLParser.Table_referenceContext c : ctx.table_references().table_reference()) {
            node.from.add((TableNode)visit(c));
        }

        for (MySQLParser.ElementContext c : ctx.column_list().element()) {
            node.columns.add((ArithNode)visit(c));
        }
        if (ctx.groupby_clause() != null) {
            node.groupby = (GroupByNode)visit(ctx.groupby_clause());
        }
        return node;
    }

    @Override
    public TreeNode visitTable_reference(@NotNull MySQLParser.Table_referenceContext ctx) {
        TableNode node = new TableNode();
        if (ctx.subquery() == null) {
            node.tableName = ctx.table_name().getText();
            node.alias = ctx.table_alias() == null ? null : ctx.table_alias().getText();
        } else {
            node.subQuery = (SelectNode)visit(ctx.subquery().select_clause());
            node.alias = ctx.table_alias() == null ? null : ctx.table_alias().getText();
        }
        return node;
    }

    @Override
    public TreeNode visitWhere_clause(@NotNull MySQLParser.Where_clauseContext ctx) {
        return visit(ctx.expression());
    }
    @Override
    public TreeNode visitExpression(@NotNull MySQLParser.ExpressionContext ctx) {
        if (ctx.and_expression().size() == 1) {
            return visit(ctx.and_expression(0));
        }
        FilterNode res = new FilterNode();
        for (MySQLParser.And_expressionContext c : ctx.and_expression()) {
            res.filters.add(visit(c));
        }
        res.op = "||";
        return res;
    }
    @Override
    public TreeNode visitAnd_expression(@NotNull MySQLParser.And_expressionContext ctx) {
        if (ctx.atom_expression().size() == 1) {
            return visit(ctx.atom_expression(0));
        }
        FilterNode res = new FilterNode();
        for (MySQLParser.Atom_expressionContext c : ctx.atom_expression()) {
            res.filters.add(visit(c));
        }
        res.op = "&&";
        return res;
    }
    @Override
    public TreeNode visitAtom_expression(@NotNull MySQLParser.Atom_expressionContext ctx) {
        if (ctx.NOT() != null) {
            FilterNode res = new FilterNode();
            res.op = "!";
            res.filters.add(visit(ctx.expression()));
            return res;
        } else if (ctx.LPAREN() != null) {
            return visit(ctx.expression());
        } else {
            return visit(ctx.simple_expression());
        }
    }
    @Override
    public TreeNode visitSimple_expression(@NotNull MySQLParser.Simple_expressionContext ctx) {
        SimpleFilterNode res = new SimpleFilterNode();
        res.left = (ArithNode)visit(ctx.element(0));
        res.right = (ArithNode)visit(ctx.element(1));
        res.op = ctx.relational_op().getText();
        return res;
    }
    @Override
    public TreeNode visitElement(@NotNull MySQLParser.ElementContext ctx) {
        if (ctx.term_element().size() == 1) {
            return visit(ctx.term_element(0));
        }
        ArithNode res = new ArithNode();
        for (MySQLParser.Term_elementContext c : ctx.term_element()) {
            res.children.add((ArithNode)visit(c));
        }
        for (MySQLParser.PlusminusContext c : ctx.plusminus()) {
            res.op.add(c.getText());
        }
        return res;
    }
    @Override
    public TreeNode visitTerm_element(@NotNull MySQLParser.Term_elementContext ctx) {
        if (ctx.factor_element().size() == 1) {
            return visit(ctx.factor_element(0));
        }
        ArithNode res = new ArithNode();
        for (MySQLParser.Factor_elementContext c : ctx.factor_element()) {
            res.children.add((ArithNode)visit(c));
        }
        for (MySQLParser.MuldivContext c : ctx.muldiv()) {
            res.op.add(c.getText());
        }
        return res;
    }
    @Override
    public TreeNode visitFactor_element(@NotNull MySQLParser.Factor_elementContext ctx) {
        ArithNode res = new ArithNode();
        if (ctx.MINUS() != null) {
            res.op.add("-");
            res.children.add((ArithNode)visit(ctx.element()));
            return res;
        } else if (ctx.function() != null) {
            res.function = ctx.function().getText();
            res.children.add((ArithNode)visit(ctx.element()));
            return res;
        } else if (ctx.LPAREN() != null) {
            return visit(ctx.element());
        } else if (ctx.column_name() != null) {
            res.columnName = ctx.column_name().getText();
            return res;
        } else {
            res.constant = ctx.getText();
            if (ctx.FLOAT() != null) {
                res.type = DataType.DOUBLE;
            } else if (ctx.INT() != null) {
                res.type = DataType.INT32;
            } else if (ctx.STR() != null) {
                res.type = DataType.STRING;
            } else {
                throw new NotImplementedException();
            }
            return res;
        }
    }
    @Override
    public TreeNode visitGroupby_clause(@NotNull MySQLParser.Groupby_clauseContext ctx) {
        GroupByNode node = new GroupByNode();
        for (MySQLParser.ElementContext c : ctx.column_list().element()) {
            node.cols.add(c.getText());
        }
        return node;
    }


}
