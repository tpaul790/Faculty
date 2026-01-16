%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern int line;
extern char* yytext;

extern void print_tables();

void yyerror(const char *s);
%}

%token PROGRAM USING NAMESPACE STD INT CHAR FLOAT STRUCT RETURN
%token IF ELSE WHILE DACA ATUNCI SFDACA
%token CIN COUT INCLUDE IOSTREAM MAIN

%token OP_SHIFT_L OP_SHIFT_R
%token OP_EQ OP_NEQ OP_LE OP_GE OP_LT OP_GT
%token AND OR
%token PLUS MINUS MUL DIV MOD
%token ASSIGN
%token SEMICOLON LBRACE RBRACE LPAREN RPAREN
%token ID CONST STRING

%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%left PLUS MINUS
%left MUL DIV MOD

%%

program:
l_includs USING NAMESPACE STD SEMICOLON INT MAIN LPAREN RPAREN LBRACE l_instr RETURN CONST SEMICOLON RBRACE
{ printf("\n === Program valid === \n"); }
;

l_includs:
include
| l_includs include
;

include:
INCLUDE OP_LT IOSTREAM OP_GT
;

l_instr:
instr
| l_instr instr
;

instr:
read
| write
| atr
| i_cond
| i_rep
| decl
| decl_init
| simple_test_instr
;

decl:
type ID SEMICOLON
;

decl_init:
type ID ASSIGN expr SEMICOLON
;

type:
INT | CHAR | FLOAT | struct_def
;

struct_def:
STRUCT LBRACE list_decl RBRACE
;

list_decl:
decl
| decl_init
| list_decl decl
| list_decl decl_init
;

read:
CIN OP_SHIFT_R ID SEMICOLON
;

write:
COUT OP_SHIFT_L expr SEMICOLON
| COUT OP_SHIFT_L mesaj OP_SHIFT_L expr SEMICOLON
;

mesaj:
STRING
;

atr:
ID ASSIGN expr SEMICOLON
;

expr:
ID
| CONST
| expr op_aritm ID
| expr op_aritm CONST
| LPAREN expr RPAREN
;

op_aritm:
PLUS | MINUS | DIV | MUL | MOD
;

i_rep:
WHILE LPAREN cond RPAREN LBRACE l_instr RBRACE
;

i_cond:
IF LPAREN cond RPAREN LBRACE l_instr RBRACE %prec LOWER_THAN_ELSE
| IF LPAREN cond RPAREN LBRACE l_instr RBRACE ELSE LBRACE l_instr RBRACE
;

cond:
cond_logic
;

cond_logic:
cond_rel
| cond_logic AND cond_rel
| cond_logic OR cond_rel
;

cond_rel:
ID
| CONST
| ID op_rel ID
| ID op_rel CONST
| LPAREN cond RPAREN
;

op_rel:
OP_LT | OP_LE | OP_GT | OP_GE | OP_EQ | OP_NEQ
;


simple_test_instr:
simple_expr SEMICOLON
;

simple_expr:
simple_factor
| simple_expr PLUS simple_factor
;

simple_factor:
CONST
| LPAREN simple_expr RPAREN
;

%%

void yyerror(const char *s) {
fprintf(stderr, "Syntax Error at line %d: %s\n", line, s);
}

int main(int argc, char **argv) {
extern FILE *yyin;
if (argc > 1) {
yyin = fopen(argv[1], "r");
if (!yyin) {
perror("Error opening file");
return 1;
}
}

yyparse();

printf("[INFO] Parsare terminata, generam fip si ts...");

print_tables();

return 0;
}