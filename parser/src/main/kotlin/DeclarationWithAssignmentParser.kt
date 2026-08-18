package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType

class DeclarationWithAssignmentParser(
    private val features: VersionFeatures,
    private val version: String
) : StatementParser {

    override fun canParse(tokens: Container): Boolean {
        if (tokens.size() < 6) return false

        val firstToken = tokens.get(0)?.type
        if (firstToken != DataType.LET_KEYWORD && firstToken != DataType.CONST_KEYWORD) {
            return false
        }

        val hasIdentifier = tokens.get(1)?.type == DataType.IDENTIFIER
        val hasColon = tokens.get(2)?.type == DataType.COLON
        val hasAssignation = findTokenIndex(tokens, DataType.ASSIGNATION) != -1

        return hasIdentifier && hasColon && hasAssignation
    }

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        val firstToken = tokens.get(0)!!
        if (firstToken.type == DataType.CONST_KEYWORD && !features.supportsConst) {
            return ASTNode(
                DataType.INVALID,
                "Error: Cannot use 'const' keyword in PrintScript $version",
                firstToken.position,
                listOf()
            )
        }

        val isConst = firstToken.type == DataType.CONST_KEYWORD
        val keyword = if (isConst) DataType.CONST_KEYWORD else DataType.LET_KEYWORD

        val identifierToken = tokens.get(1)!!
        val typeToken = tokens.get(3)!!
        val assignationIndex = findTokenIndex(tokens, DataType.ASSIGNATION)

        val valueTokens = tokens.slice(assignationIndex + 1)

        return ASTNode(
            DataType.DECLARATION,
            "=",
            tokens.get(assignationIndex)!!.position,
            listOf(
                ASTNode(
                    keyword,
                    identifierToken.content,
                    identifierToken.position,
                    listOf(
                        ASTNode(
                            DataType.IDENTIFIER,
                            identifierToken.content,
                            identifierToken.position,
                            listOf()
                        ),
                        ASTNode(
                            typeToken.type,
                            typeToken.content,
                            typeToken.position,
                            listOf()
                        )
                    )
                ),
                parser.expParse(valueTokens)
            )
        )
    }

    private fun findTokenIndex(tokens: Container, type: DataType): Int {
        for (i in 0 until tokens.size()) {
            if (tokens.get(i)?.type == type) return i
        }
        return -1
    }
}
