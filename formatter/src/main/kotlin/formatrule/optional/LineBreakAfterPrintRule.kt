package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakAfterPrintRule(private val lineBreakCount: Int = 1) : FormatRule {

    private val printlnType = DataType.PRINTLN
    private val identifier = DataType.IDENTIFIER
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE
    private val semicolon = DataType.SEMICOLON

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            // Check both PRINTLN token and IDENTIFIER with content "println"
            val isPrintln = token.type == printlnType ||
                (token.type == identifier && token.content == "println")

            if (isPrintln) {
                val semicolonIndex = findSemicolonAfterPrintln(tokens, i)
                if (semicolonIndex == -1) {
                    i++
                    continue
                }

                // FIRST: Remove ALL existing line breaks and spaces after semicolon
                var removeIndex = semicolonIndex + 1
                while (removeIndex < tokens.size()) {
                    val next = tokens.get(removeIndex)
                    if (next?.type == lineBreak || next?.type == space) {
                        val response = tokens.remove(removeIndex)
                        tokens = response.container
                        // Don't increment removeIndex, continue checking same position
                    } else {
                        break
                    }
                }

                // SECOND: Insert exactly the configured number of line breaks
                val insertPosition = semicolonIndex + 1
                repeat(lineBreakCount) { offset ->
                    tokens = tokens.addAt(
                        Token(lineBreak, "\n", Position(0, 0)),
                        insertPosition + offset
                    )
                }
            }

            i++
        }

        return tokens
    }

    private fun findSemicolonAfterPrintln(tokens: Container, startIndex: Int): Int {
        var parenCount = 0
        var j = startIndex + 1

        while (j < tokens.size()) {
            val current = tokens.get(j) ?: break
            when (current.type) {
                DataType.OPEN_PARENTHESIS -> parenCount++
                DataType.CLOSE_PARENTHESIS -> parenCount--
                semicolon -> if (parenCount == 0) return j
                else -> {}
            }
            j++
        }
        return -1
    }
}
