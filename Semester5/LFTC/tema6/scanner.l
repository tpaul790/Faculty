%{
#include <stdio.h>
#include <string.h>
#include "parser.tab.h"
%}

%option noyywrap

%%

"#include"      { return INCLUDE; }
"<iostream>"    { return IOSTREAM; }
"using"         { return USING; }
"namespace"     { return NAMESPACE; }
"std"           { return STD; }
"int"           { return INT; }
"main"          { return MAIN; }
"return"        { return RETURN; }
"cin"           { return CIN; }
"cout"          { return COUT; }
">>"            { return SHIFT_R; }
"<<"            { return SHIFT_L; }

"+"             { return PLUS; }
"-"             { return MINUS; }
"*"             { return MUL; }
"/"             { return DIV; }
"%"             { return MOD; }
"="             { return ASSIGN; }

"("             { return LPAREN; }
")"             { return RPAREN; }
"{"             { return LBRACE; }
"}"             { return RBRACE; }
";"             { return SEMI; }

[a-zA-Z_][a-zA-Z0-9_]* {
    yylval.id = strdup(yytext);
    return ID;
}

[0-9]+ {
    yylval.val = atoi(yytext);
    return CONST;
}

[ \t\n]         ; /* Ignorăm spațiile și newline */
.               { printf("Caracter necunoscut: %s\n", yytext); }

%%