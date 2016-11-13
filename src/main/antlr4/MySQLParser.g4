
parser grammar MySQLParser;
@header {
}
options
   { tokenVocab = MySQLLexer; }

stat
   : select_clause
   ;

select_clause
   : SELECT column_list FROM table_references ( where_clause )? (groupby_clause)? (orderby_clause)?
   ;

table_name
   : ID
   ;

table_alias
   : ID
   ;

column_name
   : ( table_alias DOT )? ID
   ;

column_list
   : element ( COMMA element )*
   ;

where_clause
   : WHERE expression
   ;

expression
   : left=and_expression (op=OR right=and_expression)*
   ;

and_expression
   : left=atom_expression (op=AND right=atom_expression)*
   ;

atom_expression
   : LPAREN expression RPAREN
   | NOT expression
   | simple_expression
   ;

element
   : left=term_element (op=plusminus right=term_element)*
   ;

term_element
   : left=factor_element (op=muldiv right=factor_element)*
   ;

factor_element
   : LPAREN element RPAREN
   | MINUS element
   | function LPAREN element RPAREN
   | STR
   | INT
   | FLOAT
   | column_name
   ;

relational_op
   : EQ | LTH | GTH | NOT_EQ | LET | GET
   ;

simple_expression
   : element relational_op element
   ;

table_references
   : table_reference ( COMMA table_reference )*
   ;

table_reference
   : table_name ( table_alias )?
   | subquery subquery_alias
   ;


subquery_alias
   : ID
   ;

subquery
   : LPAREN select_clause RPAREN
   ;

groupby_clause
   : GROUP BY column_list
;

orderby_clause
   : ORDER BY column_list (ASC | DESC)?
;

plusminus
   : PLUS
   | MINUS
   ;

muldiv
   : MULTIPLE
   | DIVIDE
   ;

function
   : ID
   ;