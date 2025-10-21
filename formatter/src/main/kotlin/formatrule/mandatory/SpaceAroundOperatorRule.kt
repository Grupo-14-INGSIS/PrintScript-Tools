package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class SpaceAroundOperatorRule : FormatRule {

    private val space = DataType.SPACE

    private val operators = listOf(
        DataType.ADDITION,
        DataType.SUBTRACTION,
        DataType.MULTIPLICATION,
        DataType.DIVISION
    )

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type in operators) {
                // Primero agregar espacio DESPUÉS
                if (i + 1 < tokens.size()) {
                    val next = tokens.get(i + 1)
                    if (next != null && next.type != space) {
                        tokens = tokens.addAt(
                            Token(space, " ", Position(0, 0)),
                            i + 1
                        )
                    }
                }

                // Luego agregar espacio ANTES
                if (i > 0) {
                    val previous = tokens.get(i - 1)
                    if (previous != null && previous.type != space) {
                        tokens = tokens.addAt(
                            Token(space, " ", Position(0, 0)),
                            i
                        )
                        i++ // Ajustar porque agregamos antes
                    }
                }
            }
            i++
        }

        return tokens
    }
}
