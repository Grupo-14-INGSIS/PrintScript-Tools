package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType

class IfStatementParser : StatementParser {

    override fun canParse(tokens: Container): Boolean {
        return !tokens.isEmpty() && tokens.get(0)?.type == DataType.IF_KEYWORD
    }

    override fun parse(tokens: Container, parser: Parser): ASTNode {
        return parser.ifStmtParse(tokens)
    }
}
