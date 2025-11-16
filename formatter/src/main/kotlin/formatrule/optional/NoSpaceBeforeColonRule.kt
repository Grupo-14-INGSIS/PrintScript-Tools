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
            token = tokens.get(
                i
            )
            if (token == null) break

            if (token.type == colon) {
                while (i > 0) {
                    previous = tokens.get(
                        i - 1
                    )
                    if (previous == null) {
                        break
                    } else if (previous.type == space) { // It's a space
                        val response = tokens.remove(
                            i - 1
                        )
                        tokens = response.container
                        if (response.token == null) break
                        i--
                    } else {
                        break
                    }
                }
                i++
            } else {
                i++
            }
        }
        return tokens
    }
}
