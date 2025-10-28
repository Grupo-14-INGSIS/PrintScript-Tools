package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakAfterPrintRule(private val lineBreakCount: Int = 1) : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE
    private val semicolon = DataType.SEMICOLON

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            // Buscar "println"
            if (token.type == identifier && token.content == "println") {
                // Encontrar el semicolon que cierra este println
                var semicolonIndex = findSemicolonAfterPrintln(tokens, i)

                if (semicolonIndex == -1) {
                    i++
                    continue
                }

                // Contar cuántos line breaks ya existen DESPUÉS del semicolon
                var existingLineBreaks = 0
                var j = semicolonIndex + 1

                // Contar line breaks (pueden estar intercalados con espacios)
                while (j < tokens.size()) {
                    val next = tokens.get(j) ?: break
                    if (next.type == lineBreak) {
                        existingLineBreaks++
                        j++
                    } else if (next.type == space) {
                        j++
                    } else {
                        break
                    }
                }

                // Total de line breaks que queremos: lineBreakCount + 1 (el mandatory)
                val totalNeeded = lineBreakCount + 1
                val missing = totalNeeded - existingLineBreaks

                if (missing > 0) {
                    // Insertar después del semicolon, saltando espacios
                    var insertPos = semicolonIndex + 1
                    while (insertPos < tokens.size() && tokens.get(insertPos)?.type == space) {
                        insertPos++
                    }

                    // Insertar después de los line breaks existentes
                    insertPos += existingLineBreaks

                    repeat(missing) {
                        tokens = tokens.addAt(
                            Token(lineBreak, "\n", Position(0, 0)),
                            insertPos
                        )
                    }
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
                semicolon -> {
                    if (parenCount == 0) {
                        return j
                    }
                }
                else -> {}
            }
            j++
        }
        return -1
    }
}
