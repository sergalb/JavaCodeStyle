grammar Java;

//Parser

file
    : package_? import_* classDefinition
    ;

package_
    : PACKAGE classType SEMI
    ;

import_
    :   IMPORT classType (DOT '*')? SEMI
    ;

literal
	:	IntegerLiteral
	|	FloatingPointLiteral
	|	BooleanLiteral
	|	StringLiteral
	|	NullLiteral
	;


type
	:	referenceType
	|	primitiveType
	;

typeWithVoid
    :   type
    |   VOID
    ;

primitiveType
    :   BYTE
	|	SHORT
	|	INT
	|	LONG
	|	CHAR
    |	FLOAT
    |	DOUBLE
	|	BOOLEAN
	;

referenceType
	:	classType
	|	arrayType
	;

classType
    :   Identifier (DOT Identifier)*
    ;

arrayType
    :   (primitiveType | classType) (LBRACK RBRACK)+
    ;


classDefinition
    :   accessModifiers? CLASS Identifier LBRACE (field | methodDefinition)* RBRACE
    ;

field
    :   type Identifier (ASSIGN expression)? SEMI
    ;

value
    :   Identifier (LBRACK IntegerLiteral RBRACK)+
    |   Identifier
    |   literal
    |   newWithCreation
    |   functionOrConstructorCall
    |
    ;

expression
    :   value (operator value)*
    ;
operator
    :   comparisonOperator
    |   logicOperator
    |   ADD
    |   SUB
    |   MUL
    |   DIV
    |   MOD
    ;


newWithCreation
    :   NEW (functionOrConstructorCall | arrayCreation)
    ;

functionOrConstructorCall
    :   classType LPAREN argumentList? RPAREN
    ;

argumentList
    :   argumentElement (COMMA argumentElement)*
    ;

argumentElement
    :   Identifier
    ;

arrayCreation
    :   (classType | primitiveType) (LBRACK IntegerLiteral RBRACK)+
    ;


methodDefinition
    :   accessModifiers? typeWithVoid Identifier LPAREN parametrList? RPAREN codeBlock
    ;

accessModifiers
    :   PUBLIC
    |   PROTECTED
    |   PRIVATE
    ;

parametrList
    : parametrElement (COMMA parametrElement)*
    ;

parametrElement
    :   (type Identifier)
    ;


codeBlock
    : LBRACE body* RBRACE
    ;
body
    :   field
    |   assigment
    |   functionOrConstructorCall SEMI
    |   whileLoop
    |   ifExpression
    |   returnStatement
    ;

assigment
    :   Identifier assignmentOperator value SEMI
    ;

assignmentOperator
	:	ASSIGN
	|   ADD_ASSIGN
    |   SUB_ASSIGN
    |   MUL_ASSIGN
    |   DIV_ASSIGN
    |   AND_ASSIGN
    |   OR_ASSIGN
    |   XOR_ASSIGN
    |   MOD_ASSIGN
    |   LSHIFT_ASSIGN
    |   RSHIFT_ASSIGN
    ;

whileLoop
    :   WHILE LPAREN condition RPAREN codeBlock
    ;

ifExpression
    :   IF LPAREN condition RPAREN codeBlock (ELSE codeBlock)?
    ;

condition
    :   conditionAtom (logicOperator conditionAtom)*
    ;

conditionAtom
    :   BooleanLiteral
    |   functionOrConstructorCall
    |   comparison
    ;

comparison
    :   comparisonArgument comparisonOperator comparisonArgument
    ;

comparisonArgument
    :   Identifier
    |   expression
    ;

comparisonOperator
    :   EQUAL
    |   LE
    |   GE
    |   LT
    |   GT
    |   NOTEQUAL
    ;

logicOperator
    :   BITAND
    |   BITOR
    |   CARET
    |   AND
    |   OR
    ;

returnStatement
    :   RETURN expression SEMI
    ;


// Lexer

BOOLEAN : 'boolean';
BYTE : 'byte';
CHAR : 'char';
CLASS : 'class';
CONST : 'const';
DOUBLE : 'double';
ELSE : 'else';
FLOAT : 'float';
IF : 'if';
IMPORT : 'import';
INT : 'int';
LONG : 'long';
NEW : 'new';
PACKAGE : 'package';
PRIVATE : 'private';
PROTECTED : 'protected';
PUBLIC : 'public';
RETURN : 'return';
SHORT : 'short';
VOID : 'void';
WHILE : 'while';

IntegerLiteral
	:	DecimalNumeral [lL]?
	;


fragment
DecimalNumeral
	:	'0'
	|	[1-9] Digits?
	;

fragment
Digits
    :   [0-9]+
    ;



FloatingPointLiteral
	:	Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |	'.' Digits ExponentPart? FloatTypeSuffix?
    |	Digits ExponentPart FloatTypeSuffix?
    |	Digits FloatTypeSuffix
    ;

fragment
ExponentPart
	:	ExponentIndicator SignedInteger
	;

fragment
ExponentIndicator
	:	[eE]
	;

fragment
SignedInteger
	:	Sign? Digits
	;

fragment
Sign
	:	[+-]
	;

fragment
FloatTypeSuffix
	:	[fFdD]
	;


BooleanLiteral
	:	'true'
	|	'false'
	;

NullLiteral
	:	'null'
	;

StringLiteral
	:	'"' (~["\\])* '"'
	;

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';


ASSIGN : '=';
GT : '>';
LT : '<';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
CARET : '^';
MOD : '%';

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';
AND_ASSIGN : '&=';
OR_ASSIGN : '|=';
XOR_ASSIGN : '^=';
MOD_ASSIGN : '%=';
LSHIFT_ASSIGN : '<<=';
RSHIFT_ASSIGN : '>>=';


Identifier
	:	JavaLetter JavaLetterOrDigit*
	;

fragment
JavaLetter
	:	[a-zA-Z$_]
	;

fragment
JavaLetterOrDigit
	:	[a-zA-Z0-9]
    ;


WS  :  [ \t\r\n\u000C]+ -> channel(HIDDEN)
    ;

COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(HIDDEN)
    ;
