package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceBeforeColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var token: Token?
        var previous: Token?
        var tokens = source
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
                        val response = tokens.remove(i - 1)
                        tokens = response.container
                        if (response.token == null) break
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
