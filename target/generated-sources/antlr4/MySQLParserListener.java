// Generated from MySQLParser.g4 by ANTLR 4.3


import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MySQLParser}.
 */
public interface MySQLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MySQLParser#plusminus}.
	 * @param ctx the parse tree
	 */
	void enterPlusminus(@NotNull MySQLParser.PlusminusContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#plusminus}.
	 * @param ctx the parse tree
	 */
	void exitPlusminus(@NotNull MySQLParser.PlusminusContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(@NotNull MySQLParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(@NotNull MySQLParser.StatContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void enterWhere_clause(@NotNull MySQLParser.Where_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#where_clause}.
	 * @param ctx the parse tree
	 */
	void exitWhere_clause(@NotNull MySQLParser.Where_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(@NotNull MySQLParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(@NotNull MySQLParser.SubqueryContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull MySQLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull MySQLParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(@NotNull MySQLParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(@NotNull MySQLParser.Column_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#atom_expression}.
	 * @param ctx the parse tree
	 */
	void enterAtom_expression(@NotNull MySQLParser.Atom_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#atom_expression}.
	 * @param ctx the parse tree
	 */
	void exitAtom_expression(@NotNull MySQLParser.Atom_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#term_element}.
	 * @param ctx the parse tree
	 */
	void enterTerm_element(@NotNull MySQLParser.Term_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#term_element}.
	 * @param ctx the parse tree
	 */
	void exitTerm_element(@NotNull MySQLParser.Term_elementContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void enterSimple_expression(@NotNull MySQLParser.Simple_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#simple_expression}.
	 * @param ctx the parse tree
	 */
	void exitSimple_expression(@NotNull MySQLParser.Simple_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(@NotNull MySQLParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(@NotNull MySQLParser.Table_nameContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#muldiv}.
	 * @param ctx the parse tree
	 */
	void enterMuldiv(@NotNull MySQLParser.MuldivContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#muldiv}.
	 * @param ctx the parse tree
	 */
	void exitMuldiv(@NotNull MySQLParser.MuldivContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#factor_element}.
	 * @param ctx the parse tree
	 */
	void enterFactor_element(@NotNull MySQLParser.Factor_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#factor_element}.
	 * @param ctx the parse tree
	 */
	void exitFactor_element(@NotNull MySQLParser.Factor_elementContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#table_references}.
	 * @param ctx the parse tree
	 */
	void enterTable_references(@NotNull MySQLParser.Table_referencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#table_references}.
	 * @param ctx the parse tree
	 */
	void exitTable_references(@NotNull MySQLParser.Table_referencesContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void enterSelect_clause(@NotNull MySQLParser.Select_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#select_clause}.
	 * @param ctx the parse tree
	 */
	void exitSelect_clause(@NotNull MySQLParser.Select_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#and_expression}.
	 * @param ctx the parse tree
	 */
	void enterAnd_expression(@NotNull MySQLParser.And_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#and_expression}.
	 * @param ctx the parse tree
	 */
	void exitAnd_expression(@NotNull MySQLParser.And_expressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#relational_op}.
	 * @param ctx the parse tree
	 */
	void enterRelational_op(@NotNull MySQLParser.Relational_opContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#relational_op}.
	 * @param ctx the parse tree
	 */
	void exitRelational_op(@NotNull MySQLParser.Relational_opContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#groupby_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroupby_clause(@NotNull MySQLParser.Groupby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#groupby_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroupby_clause(@NotNull MySQLParser.Groupby_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#column_list}.
	 * @param ctx the parse tree
	 */
	void enterColumn_list(@NotNull MySQLParser.Column_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#column_list}.
	 * @param ctx the parse tree
	 */
	void exitColumn_list(@NotNull MySQLParser.Column_listContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#table_reference}.
	 * @param ctx the parse tree
	 */
	void enterTable_reference(@NotNull MySQLParser.Table_referenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#table_reference}.
	 * @param ctx the parse tree
	 */
	void exitTable_reference(@NotNull MySQLParser.Table_referenceContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(@NotNull MySQLParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(@NotNull MySQLParser.Table_aliasContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#orderby_clause}.
	 * @param ctx the parse tree
	 */
	void enterOrderby_clause(@NotNull MySQLParser.Orderby_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#orderby_clause}.
	 * @param ctx the parse tree
	 */
	void exitOrderby_clause(@NotNull MySQLParser.Orderby_clauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(@NotNull MySQLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(@NotNull MySQLParser.ElementContext ctx);

	/**
	 * Enter a parse tree produced by {@link MySQLParser#subquery_alias}.
	 * @param ctx the parse tree
	 */
	void enterSubquery_alias(@NotNull MySQLParser.Subquery_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link MySQLParser#subquery_alias}.
	 * @param ctx the parse tree
	 */
	void exitSubquery_alias(@NotNull MySQLParser.Subquery_aliasContext ctx);
}