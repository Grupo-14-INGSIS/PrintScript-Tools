package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class SpaceBetweenTokensRule(private val enabled: Boolean = true) : FormatRule {

    private val space = DataType.SPACE
    private val noSpaceBefore = setOf(DataType.SEMICOLON)
    private val noSpaceAfter = emptySet<DataType>()

    override fun format(source: Container): Container {
        if (!enabled) return source

        var result = Container()
        var i = 0

        while (i < source.size()) {
            val current = source.get(i)
            val next = source.get(i + 1)

            if (current == null) {
                i++
                continue
            }

            if (current.type == space && next?.type == space) {
                i++
                continue
            }

            result = result.addContainer(current)

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
