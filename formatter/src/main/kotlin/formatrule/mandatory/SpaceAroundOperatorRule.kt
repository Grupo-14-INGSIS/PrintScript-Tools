package formatter.src.main.kotlin.formatrule.mandatory

import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class SpaceAroundOperatorRule : FormatRule {

    private val space = DataType.SPACE

    private val equals = DataType.ASSIGNATION
    private val addition = DataType.ADDITION
    private val subtraction = DataType.SUBTRACTION
    private val multiplication = DataType.MULTIPLICATION
    private val division = DataType.DIVISION

    private val operators = listOf(equals, addition, subtraction, multiplication, division)

    override fun format(tokens: Container): Boolean {
        var token: Token?
        var previous: Token?
        var next: Token?
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) {
                return false
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
                        tokens.addAt(Token(space, " ", 0), i + 1)
                    }
                }
                previous = tokens.get(i - 1)
                if (previous != null) {
                    if (previous.type != space) {
                        tokens.addAt(Token(space, " ", 0), i - 1)
                        i++
                    }
                }
            }
            i++
        }
        return true
    }
}
