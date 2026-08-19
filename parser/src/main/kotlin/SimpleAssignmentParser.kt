package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType

class SimpleAssignmentParser : StatementParser {

    override fun canParse(tokens: Container): Boolean {
        if (tokens.size() < 3) return false
        return tokens.get(0)?.type == DataType.IDENTIFIER &&
            tokens.get(1)?.type == DataType.ASSIGNATION
    }

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        val identifierToken = tokens.get(0)!!
        val assignationToken = tokens.get(1)!!
        val valueTokens = tokens.slice(2)

        return ASTNode(
            ASTNodeType.ASSIGNATION,
            "=",
            assignationToken.position,
            listOf(
                ASTNode(
                    ASTNodeType.IDENTIFIER,
                    identifierToken.content,
                    identifierToken.position,
                    listOf()
                ),
                parser.expParse(valueTokens)
            )
        )
    }
}
