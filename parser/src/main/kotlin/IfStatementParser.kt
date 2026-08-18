package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

/**
 * Parser de sentencias 'if / else' (SRP).
 * Encapsula la lógica de validación de sintaxis de condiciones y bloques de control de flujo.
 */
class IfStatementParser : StatementParser {

    private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())

    override fun canParse(tokens: Container): Boolean {
        return !tokens.isEmpty() && tokens.get(0)?.type == DataType.IF_KEYWORD
    }

    override fun parse(tokens: Container, parser: ExpressionParser): ASTNode {
        if (tokens.isEmpty()) return invalid
        val ifKeyword = tokens.get(0) ?: return invalid

        val conditionStart = findTokenIndex(tokens, DataType.OPEN_PARENTHESIS, 1)
        val conditionEnd = findMatchingClosingParenthesis(tokens, conditionStart)
        val blockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, conditionEnd + 1)
        val blockEnd = findMatchingBrace(tokens, blockStart)

        if (conditionStart == -1 || conditionEnd == -1 || blockStart == -1 || blockEnd == -1) {
            return invalid
        }

        val conditionTokens = tokens.slice(conditionStart + 1, conditionEnd)
        val condition = parser.expParse(conditionTokens)

        if (condition.type == DataType.INVALID) {
            return invalid
        }

        val ifBlockTokens = tokens.slice(blockStart + 1, blockEnd)
        val ifBlock = parser.parseBlock(ifBlockTokens)

        val children = mutableListOf(condition, ifBlock)

        val elseIndex = blockEnd + 1
        if (elseIndex < tokens.size() && tokens.get(elseIndex)?.type == DataType.ELSE_KEYWORD) {
            val elseBlockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, elseIndex + 1)
            if (elseBlockStart != -1) {
                val elseBlockEnd = findMatchingBrace(tokens, elseBlockStart)
                if (elseBlockEnd != -1) {
                    val elseBlockTokens = tokens.slice(elseBlockStart + 1, elseBlockEnd)
                    val elseBlock = parser.parseBlock(elseBlockTokens)
                    children.add(elseBlock)
                } else {
                    return invalid
                }
            } else {
                return invalid
            }
        }

        return ASTNode(
            DataType.IF_STATEMENT,
            "if",
            ifKeyword.position,
            children
        )
    }

    private fun findTokenIndex(tokens: Container, type: DataType, startFrom: Int = 0): Int {
        for (i in startFrom until tokens.size()) {
            if (tokens.get(i)?.type == type) return i
        }
        return -1
    }

    private fun findMatchingClosingParenthesis(tokens: Container, openIndex: Int): Int {
        if (openIndex < 0 || openIndex >= tokens.size()) return -1

        var parenCount = 1
        for (i in openIndex + 1 until tokens.size()) {
            when (tokens.get(i)?.type) {
                DataType.OPEN_PARENTHESIS -> parenCount++
                DataType.CLOSE_PARENTHESIS -> {
                    parenCount--
                    if (parenCount == 0) return i
                }
                else -> {}
            }
        }
        return -1
    }

    private fun findMatchingBrace(tokens: Container, openBraceIndex: Int): Int {
        if (openBraceIndex < 0 || openBraceIndex >= tokens.size()) return -1

        var braceCount = 1
        for (i in openBraceIndex + 1 until tokens.size()) {
            when (tokens.get(i)?.type) {
                DataType.OPEN_BRACE -> braceCount++
                DataType.CLOSE_BRACE -> {
                    braceCount--
                    if (braceCount == 0) return i
                }
                else -> {}
            }
        }
        return -1
    }
}
