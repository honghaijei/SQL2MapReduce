// Generated from MySQLParser.g4 by ANTLR 4.3


import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MySQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MySQLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MySQLParser#plusminus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusminus(@NotNull MySQLParser.PlusminusContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(@NotNull MySQLParser.StatContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#where_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhere_clause(@NotNull MySQLParser.Where_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(@NotNull MySQLParser.SubqueryContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull MySQLParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(@NotNull MySQLParser.Column_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#atom_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom_expression(@NotNull MySQLParser.Atom_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#term_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm_element(@NotNull MySQLParser.Term_elementContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#simple_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_expression(@NotNull MySQLParser.Simple_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(@NotNull MySQLParser.Table_nameContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#muldiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMuldiv(@NotNull MySQLParser.MuldivContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#factor_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor_element(@NotNull MySQLParser.Factor_elementContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#table_references}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_references(@NotNull MySQLParser.Table_referencesContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#select_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_clause(@NotNull MySQLParser.Select_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#and_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expression(@NotNull MySQLParser.And_expressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#relational_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational_op(@NotNull MySQLParser.Relational_opContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#groupby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupby_clause(@NotNull MySQLParser.Groupby_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#column_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_list(@NotNull MySQLParser.Column_listContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#table_reference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_reference(@NotNull MySQLParser.Table_referenceContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(@NotNull MySQLParser.Table_aliasContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#orderby_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderby_clause(@NotNull MySQLParser.Orderby_clauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(@NotNull MySQLParser.ElementContext ctx);

	/**
	 * Visit a parse tree produced by {@link MySQLParser#subquery_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery_alias(@NotNull MySQLParser.Subquery_aliasContext ctx);
}