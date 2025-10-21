package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IfBraceBelowLineRule : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val openBrace = DataType.OPEN_BRACE
    private val closeParen = DataType.CLOSE_PARENTHESIS
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == identifier && token.content == "if") {
                val closeParenIndex = findCloseParenthesis(tokens, i)

                if (closeParenIndex != -1) {
                    val braceIndex = findOpenBrace(tokens, closeParenIndex)

                    if (braceIndex != -1) {
                        // Remover todo entre ) y {
                        var currentBraceIndex = braceIndex
                        while (closeParenIndex + 1 < currentBraceIndex) {
                            val tokenBetween = tokens.get(closeParenIndex + 1)
                            if (tokenBetween?.type == lineBreak || tokenBetween?.type == space) {
                                val response = tokens.remove(closeParenIndex + 1)
                                tokens = response.container
                                if (response.token == null) break
                                currentBraceIndex--
                            } else {
                                break
                            }
                        }

                        // Agregar un line break
                        tokens = tokens.addAt(
                            Token(lineBreak, "\n", Position(0, 0)),
                            closeParenIndex + 1
                        )
                    }
                }
            }
            i++
        }

        return tokens
    }

    private fun findCloseParenthesis(tokens: Container, startIndex: Int): Int {
        var parenCount = 0
        var j = startIndex + 1

        while (j < tokens.size()) {
            val currentToken = tokens.get(j) ?: break
            when (currentToken.type) {
                DataType.OPEN_PARENTHESIS -> parenCount++
                closeParen -> {
                    parenCount--
                    if (parenCount == 0) return j
                }

                else -> {}
            }
            j++
        }
        return -1
    }

    private fun findOpenBrace(tokens: Container, startIndex: Int): Int {
        var j = startIndex + 1
        while (j < tokens.size()) {
            val currentToken = tokens.get(j) ?: break
            when (currentToken.type) {
                openBrace -> return j
                space, lineBreak -> j++
                else -> return -1
            }
        }
        return -1
    }
}
