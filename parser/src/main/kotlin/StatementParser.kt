package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container

interface StatementParser {
    fun canParse(tokens: Container): Boolean
    fun parse(tokens: Container, parser: ExpressionParser): ASTNode
}

