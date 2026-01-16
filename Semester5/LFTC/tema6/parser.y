%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void yyerror(const char *s);
int yylex();

void header_asm();
void footer_asm();
%}

%union {
    int val;
    char* id;
}

%token INCLUDE IOSTREAM USING NAMESPACE STD INT MAIN RETURN
%token CIN COUT SHIFT_R SHIFT_L
%token PLUS MINUS MUL DIV MOD ASSIGN
%token LPAREN RPAREN LBRACE RBRACE SEMI
%token <id> ID
%token <val> CONST

%%

program:
    preamble INT MAIN LPAREN RPAREN LBRACE { header_asm(); } l_instr RETURN CONST SEMI RBRACE { footer_asm(); }
    ;

preamble:
      INCLUDE IOSTREAM USING NAMESPACE STD SEMI
    |
    ;

l_instr:
      instr l_instr
    | instr
    ;

instr:
      decl
    | read_instr
    | write_instr
    | assign_instr
    ;

decl:
    INT ID SEMI {
        printf("section .data\n");
        printf("    %s dd 0\n", $2);  // Alocăm spațiu pentru variabilă
        printf("section .text\n");    // Revenim la cod
    }
    ;

read_instr:
    CIN SHIFT_R ID SEMI {
        printf("    ; cin >> %s\n", $3);
        printf("    push %s\n", $3);      // Adresa variabilei
        printf("    push format_in\n");
        printf("    call scanf\n");
        printf("    add esp, 8\n");
    }
    ;

write_instr:
    COUT SHIFT_L expression SEMI {
        printf("    ; cout << expr\n");
        printf("    pop eax\n");          // Rezultatul expresiei
        printf("    push eax\n");
        printf("    push format_out\n");
        printf("    call printf\n");
        printf("    add esp, 8\n");
    }
    ;

assign_instr:
    ID ASSIGN expression SEMI {
        printf("    ; %s = ...\n", $1);
        printf("    pop eax\n");
        printf("    mov [%s], eax\n", $1);
    }
    ;

expression:
      expression PLUS term {
          printf("    pop ebx\n");
          printf("    pop eax\n");
          printf("    add eax, ebx\n");
          printf("    push eax\n");
      }
    | expression MINUS term {
          printf("    pop ebx\n");
          printf("    pop eax\n");
          printf("    sub eax, ebx\n");
          printf("    push eax\n");
      }
    | term
    ;

term:
      term MUL factor {
          printf("    pop ebx\n");
          printf("    pop eax\n");
          printf("    imul eax, ebx\n");
          printf("    push eax\n");
      }
    | term DIV factor {
          printf("    pop ebx\n");
          printf("    pop eax\n");
          printf("    cdq\n");        // Extinde semnul EAX -> EDX:EAX
          printf("    idiv ebx\n");
          printf("    push eax\n");
      }
    | term MOD factor {
          printf("    pop ebx\n");
          printf("    pop eax\n");
          printf("    cdq\n");
          printf("    idiv ebx\n");
          printf("    push edx\n");   // Restul e în EDX
      }
    | factor
    ;

factor:
      ID {
          printf("    push dword [%s]\n", $1);
      }
    | CONST {
          printf("    push %d\n", $1);
      }
    | LPAREN expression RPAREN
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Eroare sintactică: %s\n", s);
    exit(1);
}

void header_asm() {
    printf("extern printf, scanf, exit\n\n");
    printf("section .data\n");
    printf("    format_in db \"%%d\", 0\n");
    printf("    format_out db \"%%d\", 10, 0\n"); // 10 = newline
    printf("\nsection .text\n");
    printf("global main\n");
    printf("main:\n");
}

void footer_asm() {
    printf("\n    ; return 0\n");
    printf("    push 0\n");
    printf("    call exit\n");
}

int main() {
    yyparse();
    return 0;
}