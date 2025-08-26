package formatter.src.main.kotlin.formatrule.optional

import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceBeforeEqualsRule : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(tokens: Container): Boolean {
        var token: Token?
        var previous: Token?
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) return false
            /*
             * Delete every space that comes before an equals
             */

            // Found a colon
            if (token.type == equals) {
                // For every token before the equals
                while (i > 0) {
                    // See previous token
                    previous = tokens.get(i - 1)
                    if (previous == null) {
                        break
                    } else if (previous.type == space) { // It's a space
                        // Remove it and correct i value
                        if (tokens.remove(i + 1) == null) return false
                        i--
                    } else { // It's not a space => There are no spaces before the equals
                        break
                    }
                }
            } else { // Not an equals, continue
                i++
            }
        }
        return true
    }
}
