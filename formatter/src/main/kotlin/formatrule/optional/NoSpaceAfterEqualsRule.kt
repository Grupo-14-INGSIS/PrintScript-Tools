package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceAfterEqualsRule : FormatRule {

    private val equals = DataType.ASSIGNATION
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var token: Token?
        var next: Token?
        val tokens = source.copy()
        var i = 0
        while (i < tokens.size()) {
            token = tokens.get(i)
            if (token == null) break
            /*
             * Delete every space after an equals token
             */
            if (token.type == equals) {
                next = tokens.get(i + 1)
                if (next == null) {
                    break // End of tokens
                } else if (next.type == space) {
                    if (tokens.remove(i + 1) == null) break
                } else {
                    i++
                }
            } else {
                i++
            }
        }
        return tokens
    }
}
