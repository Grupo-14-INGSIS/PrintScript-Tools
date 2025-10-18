package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakAfterSemicolonRule : FormatRule {

    private val semicolon = DataType.SEMICOLON
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == semicolon) {
                // Remover TODOS los espacios y saltos de línea después del semicolon
                var removed = 0
                while (i + 1 < tokens.size()) {
                    val nextToken = tokens.get(i + 1) ?: break
                    if (nextToken.type == lineBreak || nextToken.type == space) {
                        val response = tokens.remove(i + 1)
                        tokens = response.container
                        if (response.token == null) break
                        removed++
                    } else {
                        break
                    }
                }

                // Agregar exactamente UN salto de línea después del semicolon
                tokens = tokens.addAt(
                    Token(
                        lineBreak,
                        "\n",
                        Position(0, 0)
                    ),
                    i + 1
                )
            }
            i++
        }

        return tokens
    }
}
