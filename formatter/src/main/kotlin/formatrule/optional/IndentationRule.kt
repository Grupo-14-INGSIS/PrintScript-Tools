package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IndentationRule(private val indentSize: Int) : FormatRule {

    private val openBrace = DataType.OPEN_BRACE
    private val closeBrace = DataType.CLOSE_BRACE
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var currentIndent = 0
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == lineBreak) {
                // Eliminar espacios inmediatamente después del salto de línea
                var nextIndex = i + 1
                while (nextIndex < tokens.size() && tokens.get(nextIndex)?.type == space) {
                    val response = tokens.remove(nextIndex)
                    tokens = response.container
                }

                val nextToken = tokens.get(i + 1)
                if (nextToken != null && nextToken.type != lineBreak) {
                    val indentLevel = if (nextToken.type == closeBrace) {
                        maxOf(0, currentIndent - 1)
                    } else {
                        currentIndent
                    }

                    if (indentLevel > 0) {
                        val indentString = " ".repeat(indentSize * indentLevel)
                        tokens = tokens.addAt(
                            Token(space, indentString, Position(0, 0)),
                            i + 1
                        )
                        i++
                    }
                }
            }

            if (token.type == openBrace) {
                currentIndent++
            } else if (token.type == closeBrace) {
                currentIndent = maxOf(0, currentIndent - 1)
            }

            i++
        }

        // Eliminar saltos de línea finales (garantiza que no quede \n al final)
        while (tokens.size() > 0 && tokens.get(tokens.size() - 1)?.type == lineBreak) {
            val response = tokens.remove(tokens.size() - 1)
            tokens = response.container
        }

        return tokens
    }
}
