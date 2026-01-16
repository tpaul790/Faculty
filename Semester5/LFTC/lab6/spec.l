%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.tab.h"

int line = 1;

typedef struct {
    int tokenCode;
    int tsPosition;
} FIP_ENTRY;

typedef struct {
    int id;
    char value[256];
} TS_ENTRY;

#define MAX_SIZE 1000

FIP_ENTRY fip[MAX_SIZE];
TS_ENTRY ts_identifiers[MAX_SIZE];
TS_ENTRY ts_constants[MAX_SIZE];

int fip_count = 0;
int ts_id_count = 0;
int ts_const_count = 0;

int find_symbol(const char *lexeme, TS_ENTRY *table, int count) {
    for (int i = 0; i < count; i++) {
        if (strcmp(table[i].value, lexeme) == 0) return i + 1;
    }
    return 0;
}

int add_symbol(const char *lexeme, TS_ENTRY *table, int *count) {
    if (*count >= MAX_SIZE) return -1;
    int existing = find_symbol(lexeme, table, *count);
    if (existing != 0) return existing;
    strcpy(table[*count].value, lexeme);
    table[*count].id = *count + 1;
    (*count)++;
    return *count;
}

void add_fip_entry(int tokenCode, int tsPosition) {
    if (fip_count < MAX_SIZE) {
        fip[fip_count].tokenCode = tokenCode;
        fip[fip_count].tsPosition = tsPosition;
        fip_count++;
    }
}

void print_ts(FILE *out, const char *name, TS_ENTRY *table, int count) {
    fprintf(out, "\n--- %s ---\n", name);
    fprintf(out, "| ID | Lexema |\n");
    fprintf(out, "|----|--------|\n");
    for (int i = 0; i < count; i++) {
        fprintf(out, "| %d  | %s |\n", table[i].id, table[i].value);
    }
}

void print_tables() {
    FILE *out_fip = fopen("fip.txt", "w");
    FILE *out_ts = fopen("ts.txt", "w");

    if (!out_fip || !out_ts) {
        printf("Eroare: Nu pot crea fisierele fip.txt sau ts.txt\n");
        return;
    }

    // Print FIP
    fprintf(out_fip, "--- FORMA INTERNA A PROGRAMULUI (FIP) ---\n");
    fprintf(out_fip, "| Cod Token | Pozitie TS |\n");
    fprintf(out_fip, "|-----------|------------|\n");
    for (int i = 0; i < fip_count; i++) {
        fprintf(out_fip, "| %-9d | %-10d |\n", fip[i].tokenCode, fip[i].tsPosition);
    }

    // Print TS
    print_ts(out_ts, "TABELA DE SIMBOLURI (IDENTIFICATORI)", ts_identifiers, ts_id_count);
    print_ts(out_ts, "TABELA DE SIMBOLURI (CONSTANTE SI STRING-URI)", ts_constants, ts_const_count);

    fclose(out_fip);
    fclose(out_ts);
    printf("\n[INFO] fip.txt si ts.txt generate cu succes.\n");
}
%}

%%

[\n]                { line++; }
[ \t\r]+            ;

"program"           { add_fip_entry(PROGRAM, 0); return PROGRAM; }
"using"             { add_fip_entry(USING, 0); return USING; }
"namespace"         { add_fip_entry(NAMESPACE, 0); return NAMESPACE; }
"std"               { add_fip_entry(STD, 0); return STD; }
"int"               { add_fip_entry(INT, 0); return INT; }
"main"              { add_fip_entry(MAIN, 0); return MAIN; }
"char"              { add_fip_entry(CHAR, 0); return CHAR; }
"float"             { add_fip_entry(FLOAT, 0); return FLOAT; }
"struct"            { add_fip_entry(STRUCT, 0); return STRUCT; }
"return"            { add_fip_entry(RETURN, 0); return RETURN; }
"if"                { add_fip_entry(IF, 0); return IF; }
"else"              { add_fip_entry(ELSE, 0); return ELSE; }
"while"             { add_fip_entry(WHILE, 0); return WHILE; }
"cin"               { add_fip_entry(CIN, 0); return CIN; }
"cout"              { add_fip_entry(COUT, 0); return COUT; }
"#include"          { add_fip_entry(INCLUDE, 0); return INCLUDE; }
"iostream"          { add_fip_entry(IOSTREAM, 0); return IOSTREAM; }

"<<"                { add_fip_entry(OP_SHIFT_L, 0); return OP_SHIFT_L; }
">>"                { add_fip_entry(OP_SHIFT_R, 0); return OP_SHIFT_R; }
"=="                { add_fip_entry(OP_EQ, 0); return OP_EQ; }
"!="                { add_fip_entry(OP_NEQ, 0); return OP_NEQ; }
"<="                { add_fip_entry(OP_LE, 0); return OP_LE; }
">="                { add_fip_entry(OP_GE, 0); return OP_GE; }
"<"                 { add_fip_entry(OP_LT, 0); return OP_LT; }
">"                 { add_fip_entry(OP_GT, 0); return OP_GT; }
"&&"                { add_fip_entry(AND, 0); return AND; }
"||"                { add_fip_entry(OR, 0); return OR; }
"+"                 { add_fip_entry(PLUS, 0); return PLUS; }
"-"                 { add_fip_entry(MINUS, 0); return MINUS; }
"*"                 { add_fip_entry(MUL, 0); return MUL; }
"/"                 { add_fip_entry(DIV, 0); return DIV; }
"%"                 { add_fip_entry(MOD, 0); return MOD; }
"="                 { add_fip_entry(ASSIGN, 0); return ASSIGN; }
";"                 { add_fip_entry(SEMICOLON, 0); return SEMICOLON; }
"{"                 { add_fip_entry(LBRACE, 0); return LBRACE; }
"}"                 { add_fip_entry(RBRACE, 0); return RBRACE; }
"("                 { add_fip_entry(LPAREN, 0); return LPAREN; }
")"                 { add_fip_entry(RPAREN, 0); return RPAREN; }

[A-Za-z_][A-Za-z0-9_]* {
    int pos = add_symbol(yytext, ts_identifiers, &ts_id_count);
    add_fip_entry(ID, pos);
    return ID;
}

[0-9]+ {
    int pos = add_symbol(yytext, ts_constants, &ts_const_count);
    add_fip_entry(CONST, pos);
    return CONST;
}

\"[a-z =]*\" {
    int pos = add_symbol(yytext, ts_constants, &ts_const_count);
    add_fip_entry(STRING, pos);
    return STRING;
}

. {
    printf("Lexical error at line %d: unexpected character '%s'\n", line, yytext);
}

%%

int yywrap() { return 1; }