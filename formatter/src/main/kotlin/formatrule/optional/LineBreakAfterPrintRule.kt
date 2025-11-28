package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class LineBreakAfterPrintRule(private val lineBreaks: Int) : FormatRule {
    override fun format(statements: List<Container>): List<Container> {
        val source = Container(statements.flatMap { it.container })

        val newTokens = mutableListOf<Token>()
        var i = 0
        while (i < source.size()) {
            val currentToken = source.get(i)!!
            newTokens.add(currentToken)

            if (currentToken.type == DataType.PRINTLN) {
                var semicolonIndex = -1
                for (j in i + 1 until source.size()) {
                    if (source.get(j)!!.type == DataType.SEMICOLON) {
                        semicolonIndex = j
                        break
                    }
                }

                if (semicolonIndex != -1) {
                    for (j in i + 1..semicolonIndex) {
                        newTokens.add(source.get(j)!!)
                    }
                    i = semicolonIndex

                    var isLastStatement = true
                    for (k in semicolonIndex + 1 until source.size()) {
                        val nextType = source.get(k)!!.type
                        if (nextType != DataType.SPACE && nextType != DataType.LINE_BREAK) {
                            isLastStatement = false
                            break
                        }
                    }

                    if (!isLastStatement) {
                        var nextTokenIndex = semicolonIndex + 1
                        while (nextTokenIndex < source.size() && source.get(nextTokenIndex)!!.type == DataType.LINE_BREAK) {
                            nextTokenIndex++
                        }

                        val breaksToAdd = lineBreaks + 1
                        for (k in 0 until breaksToAdd) {
                            newTokens.add(Token(DataType.LINE_BREAK, "\n", currentToken.position))
                        }

                        i = nextTokenIndex - 1
                    } else {
                        i = source.size()
                    }
                }
            }
            i++
        }
        return listOf(Container(newTokens))
    }
}
