package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import formatter.src.main.kotlin.formatrule.FormatRule

class NoSpaceAfterColonRule : FormatRule {

    private val colon = DataType.COLON
    private val space = DataType.SPACE

    override fun format(statements: List<Container>): List<Container> {
        val source = Container(statements.flatMap { it.container })

        var tokens = source
        var i = 0
        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == colon) {
                val next = tokens.get(i + 1)
                if (next == null) {
                    break // End of tokens
                } else if (next.type == space) {
                    val response = tokens.remove(i + 1)
                    tokens = response.container
                    if (response.token == null) break
                } else {
                    i++
                }
            } else {
                i++
            }
        }
        return listOf(tokens)
    }
}

