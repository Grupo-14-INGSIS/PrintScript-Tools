package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class SpaceAfterColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(statements: List<Container>): List<Container> {
        val source = Container(statements.flatMap { it.container })

        var tokens = source
        var i = 0
        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == colon) {
                while (i + 1 < tokens.size()) {
                    val next = tokens.get(i + 1)
                    if (next?.type == space) {
                        val response = tokens.remove(i + 1)
                        tokens = response.container
                        if (response.token == null) break
                    } else {
                        break
                    }
                }

                tokens = tokens.addAt(
                    Token(space, " ", Position(0, 0)),
                    i + 1
                )
            }
            i++
        }
        return listOf(tokens)
    }
}
