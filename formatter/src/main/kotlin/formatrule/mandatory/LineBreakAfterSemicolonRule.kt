package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakAfterSemicolonRule : FormatRule {

    private val semicolon = DataType.SEMICOLON
    private val lineBreak = DataType.LINE_BREAK

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(
                i
            ) ?: break

            if (token.type == semicolon) {
                val next = tokens.get(
                    i + 1
                )

                // Si no hay token siguiente o no es un salto de línea
                if (next == null) {
                    // Agregar salto de línea al final
                    tokens = tokens.addAt(
                        Token(
                            lineBreak,
                            "\n",
                            Position(
                                0,
                                0
                            )
                        ),
                        i + 1
                    )
                } else if (next.type != lineBreak) {
                    // Insertar salto de línea después del punto y coma
                    tokens = tokens.addAt(
                        Token(
                            lineBreak,
                            "\n",
                            Position(
                                0,
                                0
                            )
                        ),
                        i + 1
                    )
                }
            }
            i++
        }

        return tokens
    }
}
