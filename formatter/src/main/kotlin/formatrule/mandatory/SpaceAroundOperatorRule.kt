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
        var token: Token?
        var previous: Token?
        var next: Token?
        var tokens = source
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) {
                break
            } else if (token.type in operators) {
                /*
                 * Check first the right space and then the left token,
                 * as otherwise will modify the current indexes.
                 * This is the reason why there is a double i++ at the
                 * end of the `previous` insertion
                 */
                next = tokens.get(i + 1)
                if (next != null) {
                    if (next.type != space) {
                        tokens = tokens.addAt(Token(space, " ", Position(0, 0)), i + 1)
                    }
                }
                previous = tokens.get(i - 1)
                if (previous != null) {
                    if (previous.type != space) {
                        tokens = tokens.addAt(Token(space, " ", Position(0, 0)), i - 1)
                        i++
                    }
                }
            }
            i++
        }
        return tokens
    }
}
