package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class IndentationRule(private val indentSize: Int) : FormatRule {
    override fun format(statements: List<Container>): List<Container> {
        val source = Container(statements.flatMap { it.container })

        val newTokens = mutableListOf<Token>()
        var indentationLevel = 0
        val indent = " ".repeat(indentSize)
        var isAtLineStart = true

        for (i in 0 until source.size()) {
            val currentToken = source.get(i)!!

            if (isAtLineStart) {
                if (currentToken.type == DataType.SPACE) {
                    continue
                }

                isAtLineStart = false
                val levelForCurrentLine = if (currentToken.type == DataType.CLOSE_BRACE) {
                    (indentationLevel - 1).coerceAtLeast(0)
                } else {
                    indentationLevel
                }

                if (levelForCurrentLine > 0) {
                    newTokens.add(Token(DataType.SPACE, indent.repeat(levelForCurrentLine), currentToken.position))
                }
            }

            newTokens.add(currentToken)

            when (currentToken.type) {
                DataType.OPEN_BRACE -> indentationLevel++
                DataType.CLOSE_BRACE -> indentationLevel = (indentationLevel - 1).coerceAtLeast(0)
                DataType.LINE_BREAK -> isAtLineStart = true
                else -> { /* No hacer nada */ }
            }
        }
        return listOf(Container(newTokens))
    }
}
