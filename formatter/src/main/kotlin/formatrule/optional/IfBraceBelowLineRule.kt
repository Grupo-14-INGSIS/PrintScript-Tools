package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IfBraceBelowLineRule : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val openBrace = DataType.OPEN_BRACE
    private val closeBrace = DataType.CLOSE_BRACE
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
                if (closeParenIndex == -1) {
                    i++
                    continue
                }

                val nextToken = tokens.get(closeParenIndex + 1)
                if (nextToken?.type == openBrace) {
                    // Ya tiene llave, moverla a la línea siguiente
                    tokens = removeSpacesAndLineBreaks(tokens, closeParenIndex + 1)
                    tokens = tokens.addAt(
                        Token(lineBreak, "\n", Position(0, 0)),
                        closeParenIndex + 1
                    )

                    // Agregar indentación al contenido dentro del if
                    tokens = addIndentationAfterOpenBrace(tokens, closeParenIndex + 2)
                } else {
                    val insertOpenIndex = closeParenIndex + 1
                    tokens = removeSpacesAndLineBreaks(tokens, insertOpenIndex)

                    // Insertar salto de línea y llave de apertura
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), insertOpenIndex)
                    tokens = tokens.addAt(Token(openBrace, "{", Position(0, 0)), insertOpenIndex + 1)
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), insertOpenIndex + 2)

                    // Buscar fin de la instrucción (hasta el punto y coma)
                    var endIndex = insertOpenIndex + 3
                    while (endIndex < tokens.size()) {
                        val t = tokens.get(endIndex)
                        if (t?.content == ";") {
                            break
                        }
                        endIndex++
                    }

                    // Insertar salto de línea y llave de cierre
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), endIndex + 1)
                    tokens = tokens.addAt(Token(closeBrace, "}", Position(0, 0)), endIndex + 2)

                    // Agregar indentación (4 espacios)
                    tokens = tokens.addAt(Token(space, "    ", Position(0, 0)), insertOpenIndex + 3)
                }
            }

            i++
        }

        return tokens
    }

    private fun addIndentationAfterOpenBrace(tokens: Container, openBraceIndex: Int): Container {
        var result = tokens

        // Buscar el line break después de la llave de apertura
        var i = openBraceIndex
        while (i < result.size()) {
            val token = result.get(i)
            if (token?.type == lineBreak) {
                // Después del line break, agregar indentación si no hay espacios
                val nextToken = result.get(i + 1)
                if (nextToken != null && nextToken.type != space && nextToken.type != closeBrace) {
                    result = result.addAt(
                        Token(space, "    ", Position(0, 0)),
                        i + 1
                    )
                }
                break
            }
            i++
        }

        return result
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

    private fun removeSpacesAndLineBreaks(tokens: Container, startIndex: Int): Container {
        var result = tokens
        var j = startIndex
        while (j < result.size()) {
            val t = result.get(j)
            if (t?.type == space || t?.type == lineBreak) {
                val response = result.remove(j)
                result = response.container
            } else {
                break
            }
        }
        return result
    }
}
