package formatter.src.main.kotlin.formatrule.mandatory

import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

/** Must have as much one space between tokens */
class SpaceBetweenTokensRule : FormatRule {

    private val space = DataType.SPACE

    // It's not necessary to have at least one space between each of this tokens
    private val exceptions = listOf(
        DataType.COLON,
        DataType.SEMICOLON,
        DataType.ADDITION,
        DataType.SUBTRACTION,
        DataType.MULTIPLICATION,
        DataType.DIVISION,
        DataType.ASSIGNATION,
        DataType.OPEN_PARENTHESIS,
        DataType.CLOSE_PARENTHESIS
    )

    override fun format(source: Container): Container {
        var firstToken: Token?
        var secondToken: Token?
        val tokens = source.copy()
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
                    if (tokens.remove(i + 1) == null) break
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
