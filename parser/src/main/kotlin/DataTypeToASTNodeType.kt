package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNodeType
import tokendata.src.main.kotlin.DataType

fun DataType?.toASTNodeType(): ASTNodeType? = when (this) {
    DataType.LET_KEYWORD -> ASTNodeType.LET_KEYWORD
    DataType.CONST_KEYWORD -> ASTNodeType.CONST_KEYWORD
    DataType.IF_KEYWORD -> ASTNodeType.IF_KEYWORD
    DataType.ELSE_KEYWORD -> ASTNodeType.ELSE_KEYWORD
    DataType.DECLARATION -> ASTNodeType.DECLARATION
    DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION -> ASTNodeType.VAR_DECLARATION_WITHOUT_ASSIGNATION
    DataType.ASSIGNATION -> ASTNodeType.ASSIGNATION
    DataType.IDENTIFIER -> ASTNodeType.IDENTIFIER
    DataType.STRING_TYPE -> ASTNodeType.STRING_TYPE
    DataType.NUMBER_TYPE -> ASTNodeType.NUMBER_TYPE
    DataType.BOOLEAN_TYPE -> ASTNodeType.BOOLEAN_TYPE
    DataType.STRING_LITERAL -> ASTNodeType.STRING_LITERAL
    DataType.NUMBER_LITERAL -> ASTNodeType.NUMBER_LITERAL
    DataType.BOOLEAN_LITERAL -> ASTNodeType.BOOLEAN_LITERAL
    DataType.ADDITION -> ASTNodeType.ADDITION
    DataType.SUBTRACTION -> ASTNodeType.SUBTRACTION
    DataType.MULTIPLICATION -> ASTNodeType.MULTIPLICATION
    DataType.DIVISION -> ASTNodeType.DIVISION
    DataType.PRINTLN -> ASTNodeType.PRINTLN
    DataType.FUNCTION_CALL -> ASTNodeType.FUNCTION_CALL
    DataType.READ_INPUT -> ASTNodeType.READ_INPUT
    DataType.READ_ENV -> ASTNodeType.READ_ENV
    DataType.IF_STATEMENT -> ASTNodeType.IF_STATEMENT
    DataType.BLOCK -> ASTNodeType.BLOCK
    DataType.SCRIPT -> ASTNodeType.SCRIPT
    DataType.INVALID -> ASTNodeType.INVALID
    null -> null
    else -> ASTNodeType.INVALID
}
