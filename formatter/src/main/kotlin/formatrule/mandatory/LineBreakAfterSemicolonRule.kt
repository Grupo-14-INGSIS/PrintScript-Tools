package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakAfterSemicolonRule(private val enabled: Boolean = true) : FormatRule {

    private val semicolon = DataType.SEMICOLON
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE
    private val closeBrace = DataType.CLOSE_BRACE

    override fun format(source: Container): Container {
        if (!enabled) return source

        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == semicolon) {
                var hasContentAfter = false
                var j = i + 1
                while (j < tokens.size()) {
                    val nextToken = tokens.get(j) ?: break
                    if (nextToken.type != lineBreak && nextToken.type != space) {
                        // *** CORRECCIÓN: No agregar linebreak si viene '}' ***
                        if (nextToken.type == closeBrace) {
                            break
                        }
                        hasContentAfter = true
                        break
                    }
                    j++
                }

                if (hasContentAfter) {
                    while (i + 1 < tokens.size()) {
                        val nextToken = tokens.get(i + 1) ?: break
                        if (nextToken.type == lineBreak || nextToken.type == space) {
                            val response = tokens.remove(i + 1)
                            tokens = response.container
                            if (response.token == null) break
                        } else {
                            break
                        }
                    }

                    tokens = tokens.addAt(
                        Token(lineBreak, "\n", Position(0, 0)),
                        i + 1
                    )
                }
            }
            i++
        }

        return tokens
    }
}
