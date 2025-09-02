package formatter.src.main.kotlin.formatrule.optional

import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceAfterColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(tokens: Container): Boolean {
        var token: Token?
        var next: Token?
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) return false
            /*
             * Delete every space after a colon token
             */
            if (token.type == colon) {
                next = tokens.get(i + 1)
                if (next == null) {
                    break // End of tokens
                } else if (next.type == space) {
                    if (tokens.remove(i + 1) == null) return false
                }
            } else {
                i++
            }
        }
        return true
    }
}
