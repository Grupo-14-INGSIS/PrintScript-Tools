package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
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
                ASTNodeType.INVALID,
                "Error: Cannot use 'const' keyword in PrintScript $version",
                firstToken.position.toAstPosition(),
                listOf()
            )
        }

        val isConst = firstToken.type == DataType.CONST_KEYWORD
        val keyword = if (isConst) ASTNodeType.CONST_KEYWORD else ASTNodeType.LET_KEYWORD

        val identifierToken = tokens.get(1)!!
        val typeToken = tokens.get(3)!!

        if (typeToken.content.lowercase() !in features.types) {
            return ASTNode(
                ASTNodeType.INVALID,
                "Error: Unknown or unsupported type '${typeToken.content}' in PrintScript $version",
                typeToken.position.toAstPosition(),
                listOf()
            )
        }

        val assignationIndex = findTokenIndex(tokens, DataType.ASSIGNATION)

        val rawValueTokens = tokens.slice(assignationIndex + 1)
        val semicolonIndex = findTokenIndex(rawValueTokens, DataType.SEMICOLON)
        val valueTokens = if (semicolonIndex != -1) rawValueTokens.slice(0, semicolonIndex) else rawValueTokens
        val valueNode = parser.expParse(valueTokens)
        if (valueNode.type == ASTNodeType.INVALID) {
            return valueNode
        }

        return ASTNode(
            ASTNodeType.DECLARATION,
            "=",
            tokens.get(assignationIndex)!!.position.toAstPosition(),
            listOf(
                ASTNode(
                    keyword,
                    identifierToken.content,
                    identifierToken.position.toAstPosition(),
                    listOf(
                        ASTNode(
                            ASTNodeType.IDENTIFIER,
                            identifierToken.content,
                            identifierToken.position.toAstPosition(),
                            listOf()
                        ),
                        ASTNode(
                            typeToken.type.toASTNodeType(),
                            typeToken.content,
                            typeToken.position.toAstPosition(),
                            listOf()
                        )
                    )
                ),
                valueNode
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
