package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class AssignSpacingRule( // manejo espacio del =
    private val spaceBefore: Boolean = true,
    private val spaceAfter: Boolean = true
) : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i)
            if (token == null) break

            if (token.type == equals) {
                // PRIMERO: Remover TODOS los espacios antes del =
                while (i > 0) {
                    val previous = tokens.get(i - 1)
                    if (previous?.type == space) {
                        val response = tokens.remove(i - 1)
                        tokens = response.container
                        if (response.token == null) break
                        i--
                    } else {
                        break
                    }
                }

                // SEGUNDO: Remover TODOS los espacios después del =
                while (i + 1 < tokens.size()) {
                    val next = tokens.get(i + 1)
                    if (next?.type == space) {
                        val response = tokens.remove(i + 1)
                        tokens = response.container
                        if (response.token == null) break
                    } else {
                        break
                    }
                }

                // TERCERO: Agregar espacios según la configuración
                if (spaceAfter) {
                    tokens = tokens.addAt(
                        Token(space, " ", Position(0, 0)),
                        i + 1
                    )
                }

                if (spaceBefore) {
                    tokens = tokens.addAt(
                        Token(space, " ", Position(0, 0)),
                        i
                    )
                    i++ // Ajustar índice porque agregamos antes
                }
            }
            i++
        }

        return tokens
    }
}
