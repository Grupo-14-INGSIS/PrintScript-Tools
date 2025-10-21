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
                if (i > 0) {
                    val previous = tokens.get(i - 1)
                    if (previous != null && previous.type != space) {
                        tokens = tokens.addAt(
                            Token(space, " ", Position(0, 0)),
                            i
                        )
                        i++ // Adjust because we added before the colon
                    }
                }
            }
            i++
        }

        return tokens
    }
}
