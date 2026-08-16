package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container

class ExpressionStatementParser : StatementParser {

    override fun canParse(tokens: Container): Boolean {
        return !tokens.isEmpty()
    }

    override fun parse(tokens: Container, parser: Parser): ASTNode {
        return parser.expParse(tokens)
    }
}
