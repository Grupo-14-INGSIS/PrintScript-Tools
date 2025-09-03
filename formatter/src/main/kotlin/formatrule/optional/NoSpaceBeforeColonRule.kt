package formatter.src.main.kotlin.formatrule.optional

import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceBeforeColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var token: Token?
        var previous: Token?
        val tokens = source.copy()
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) break
            /*
             * Delete every space that comes before a colon
             * IDEA:
             * Iterate through every token
             * If there is a colon token, check previous token
             * Should check first that is not outOfBounds
             * If it is a space, remove it, decrease the token (due to array modification) and check again
             * Else, continue
             */

            // Found a colon
            if (token.type == colon) {
                // For every token before the colon
                while (i > 0) {
                    // See previous token
                    previous = tokens.get(i - 1)
                    if (previous == null) {
                        break
                    } else if (previous.type == space) { // It's a space
                        // Remove it and correct i value
                        if (tokens.remove(i - 1) == null) break
                        i--
                    } else { // It's not a space => There are no spaces before the colon
                        break
                    }
                }
                i++
            } else { // Not a colon, continue
                i++
            }
        }
        return tokens
    }
}
