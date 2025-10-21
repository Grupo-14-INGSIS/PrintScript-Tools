package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class SpaceBeforeColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == colon) {
                // Primero remover TODOS los espacios antes del colon
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

                // Luego agregar UN espacio antes del colon
                tokens = tokens.addAt(
                    Token(space, " ", Position(0, 0)),
                    i
                )
                i++ // Ajustar porque agregamos antes del colon
            }
            i++
        }

        return tokens
    }
}
