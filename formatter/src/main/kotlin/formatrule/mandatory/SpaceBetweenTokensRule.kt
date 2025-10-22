package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class SpaceBetweenTokensRule : FormatRule {

    private val space = DataType.SPACE

    // Tokens que NO deben tener espacio antes
    private val noSpaceBefore = setOf(
        DataType.SEMICOLON
        // DataType.OPEN_PARENTHESIS ← eliminado para permitir println ( x )
    )

    // Tokens que NO deben tener espacio después (vacío por ahora)
    private val noSpaceAfter = emptySet<DataType>()

    override fun format(source: Container): Container {
        var result = Container()
        var i = 0

        while (i < source.size()) {
            val current = source.get(i)
            val next = source.get(i + 1)

            if (current == null) {
                i++
                continue
            }

            // Eliminar espacios duplicados
            if (current.type == space && next?.type == space) {
                i++ // saltar espacio redundante
                continue
            }

            result = result.addContainer(current)

            // Insertar espacio si falta entre dos tokens válidos
            if (
                current.type != space &&
                next != null &&
                next.type != space &&
                next.type !in noSpaceBefore &&
                current.type !in noSpaceAfter
            ) {
                val spaceToken = Token(space, " ", current.position)
                result = result.addContainer(spaceToken)
            }

            i++
        }

        return result
    }
}
