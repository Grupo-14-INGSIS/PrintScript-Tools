package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType

class DeclarationWithoutAssignmentParser(
    private val features: VersionFeatures,
    private val version: String
) : StatementParser {

    override fun canParse(tokens: Container): Boolean {
        if (tokens.size() != 4) return false

        val firstToken = tokens.get(0)?.type
        if (firstToken != DataType.LET_KEYWORD && firstToken != DataType.CONST_KEYWORD) {
            return false
        }

        val hasIdentifier = tokens.get(1)?.type == DataType.IDENTIFIER
        val hasColon = tokens.get(2)?.type == DataType.COLON
        val hasType = tokens.get(3)?.type == DataType.STRING_TYPE ||
            tokens.get(3)?.type == DataType.NUMBER_TYPE ||
            tokens.get(3)?.type == DataType.BOOLEAN_TYPE
        val hasAssignation = findTokenIndex(tokens, DataType.ASSIGNATION) != -1

        return hasIdentifier && hasColon && hasType && !hasAssignation
    }

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        val keywordToken = tokens.get(0)!!
        if (keywordToken.type == DataType.CONST_KEYWORD && !features.supportsConst) {
            return ASTNode(
                DataType.INVALID,
                "Error: Cannot use 'const' keyword in PrintScript $version",
                keywordToken.position,
                listOf()
            )
        }

        val identifierToken = tokens.get(1)!!
        val typeToken = tokens.get(3)!!

        return ASTNode(
            DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION,
            "",
            keywordToken.position,
            listOf(
                ASTNode(
                    keywordToken.type,
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
                )
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
