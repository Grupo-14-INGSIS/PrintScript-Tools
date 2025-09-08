package formatter.src.main.kotlin.formatrule.mandatory

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

/** Must have as much one space between tokens */
class SpaceBetweenTokensRule : FormatRule {

    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var firstToken: Token?
        var secondToken: Token?
        var tokens = source
        var i = 0
        while (i < tokens.size()) {
            firstToken = tokens.get(i)
            if (firstToken == null) break
            /*
             * Keep every non-space token
             * Keep a space only if the previous one was not a space
             * Which means:
             * Delete every space token that comes after a space token
             * Continue only if the next token is a non-space token
             */
            if (firstToken.type == space) {
                secondToken = tokens.get(i + 1)
                if (secondToken == null) {
                    break // End of tokens
                } else if (secondToken.type == space) {
                    val response = tokens.remove(i + 1)
                    tokens = response.container
                    if (response.token == null) break
                } else { // Non-space token after space -> continue
                    i++
                }
            } else {
                i++
            }
        }
        return tokens
    }
}
