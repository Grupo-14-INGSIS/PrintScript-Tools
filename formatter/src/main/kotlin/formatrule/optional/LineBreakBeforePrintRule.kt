package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class LineBreakBeforePrintRule(private val count: Int = 1) : FormatRule {

    override fun format(statements: List<Container>): List<Container> {
        val source = Container(statements.flatMap { it.container })

        val newTokens = mutableListOf<Token>()
        val breaksToAdd = count + 1

        for (i in 0 until source.size()) {
            val currentToken = source.get(i)!!

            if (currentToken.type == DataType.PRINTLN) {
                while (newTokens.isNotEmpty() && (
                    newTokens.last().type == DataType.LINE_BREAK ||
                        newTokens.last().type == DataType.SPACE
                    )
                ) {
                    newTokens.removeLast()
                }

                for (k in 0 until breaksToAdd) {
                    newTokens.add(Token(DataType.LINE_BREAK, "\n", currentToken.position))
                }
            }
            newTokens.add(currentToken)
        }
        return listOf(Container(newTokens))
    }
}
