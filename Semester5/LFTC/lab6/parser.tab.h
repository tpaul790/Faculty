/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     PROGRAM = 258,
     USING = 259,
     NAMESPACE = 260,
     STD = 261,
     INT = 262,
     CHAR = 263,
     FLOAT = 264,
     STRUCT = 265,
     RETURN = 266,
     IF = 267,
     ELSE = 268,
     WHILE = 269,
     DACA = 270,
     ATUNCI = 271,
     SFDACA = 272,
     CIN = 273,
     COUT = 274,
     INCLUDE = 275,
     IOSTREAM = 276,
     MAIN = 277,
     OP_SHIFT_L = 278,
     OP_SHIFT_R = 279,
     OP_EQ = 280,
     OP_NEQ = 281,
     OP_LE = 282,
     OP_GE = 283,
     OP_LT = 284,
     OP_GT = 285,
     AND = 286,
     OR = 287,
     PLUS = 288,
     MINUS = 289,
     MUL = 290,
     DIV = 291,
     MOD = 292,
     ASSIGN = 293,
     SEMICOLON = 294,
     LBRACE = 295,
     RBRACE = 296,
     LPAREN = 297,
     RPAREN = 298,
     ID = 299,
     CONST = 300,
     STRING = 301,
     LOWER_THAN_ELSE = 302
   };
#endif
/* Tokens.  */
#define PROGRAM 258
#define USING 259
#define NAMESPACE 260
#define STD 261
#define INT 262
#define CHAR 263
#define FLOAT 264
#define STRUCT 265
#define RETURN 266
#define IF 267
#define ELSE 268
#define WHILE 269
#define DACA 270
#define ATUNCI 271
#define SFDACA 272
#define CIN 273
#define COUT 274
#define INCLUDE 275
#define IOSTREAM 276
#define MAIN 277
#define OP_SHIFT_L 278
#define OP_SHIFT_R 279
#define OP_EQ 280
#define OP_NEQ 281
#define OP_LE 282
#define OP_GE 283
#define OP_LT 284
#define OP_GT 285
#define AND 286
#define OR 287
#define PLUS 288
#define MINUS 289
#define MUL 290
#define DIV 291
#define MOD 292
#define ASSIGN 293
#define SEMICOLON 294
#define LBRACE 295
#define RBRACE 296
#define LPAREN 297
#define RPAREN 298
#define ID 299
#define CONST 300
#define STRING 301
#define LOWER_THAN_ELSE 302




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;

