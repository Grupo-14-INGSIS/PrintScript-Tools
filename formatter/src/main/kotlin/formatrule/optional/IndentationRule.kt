package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IndentationRule(private val indentSize: Int) : FormatRule {

    private val openBrace = DataType.OPEN_BRACE
    private val closeBrace = DataType.CLOSE_BRACE
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var currentIndent = 0
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            // Cuando encontramos un line break
            if (token.type == lineBreak) {
                println("Found LINE_BREAK at index $i, currentIndent=$currentIndent") // DEBUG

                // Limpiar espacios después del line break
                var nextIndex = i + 1
                while (nextIndex < tokens.size() && tokens.get(nextIndex)?.type == space) {
                    println("  Removing space at $nextIndex") // DEBUG
                    val response = tokens.remove(nextIndex)
                    tokens = response.container
                }

                // Ver qué viene después
                val nextToken = tokens.get(i + 1)
                println("  Next token: ${nextToken?.type}, content='${nextToken?.content}'") // DEBUG

                if (nextToken != null && nextToken.type != lineBreak) {
                    val indentLevel = if (nextToken.type == closeBrace) {
                        maxOf(0, currentIndent - 1)
                    } else {
                        currentIndent
                    }

                    println("  Adding indent level $indentLevel") // DEBUG

                    if (indentLevel > 0) {
                        val indentString = " ".repeat(indentSize * indentLevel)
                        tokens = tokens.addAt(
                            Token(space, indentString, Position(0, 0)),
                            i + 1
                        )
                        i++
                    }
                }
            }

            // Actualizar nivel cuando encontramos llaves
            if (token.type == openBrace) {
                println("Found OPEN_BRACE at index $i, incrementing indent to ${currentIndent + 1}") // DEBUG
                currentIndent++
            } else if (token.type == closeBrace) {
                println("Found CLOSE_BRACE at index $i, decrementing indent to ${currentIndent - 1}") // DEBUG
                currentIndent = maxOf(0, currentIndent - 1)
            }

            i++
        }

        return tokens
    }
}
