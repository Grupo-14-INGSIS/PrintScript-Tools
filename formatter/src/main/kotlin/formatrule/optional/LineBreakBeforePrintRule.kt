package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakBeforePrintRule(private val lineBreakCount: Int) : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            // Buscar "println"
            if (token.type == identifier && token.content == "println") {
                // Contar cuántos line breaks hay antes del println (ignorando espacios)
                var existingLineBreaks = 0
                var j = i - 1

                // Saltar espacios primero
                while (j >= 0) {
                    val prevToken = tokens.get(j) ?: break
                    if (prevToken.type == space) {
                        j--
                    } else {
                        break
                    }
                }

                // Contar line breaks
                while (j >= 0) {
                    val prevToken = tokens.get(j) ?: break
                    if (prevToken.type == lineBreak) {
                        existingLineBreaks++
                        j--
                    } else {
                        break
                    }
                }

                val neededLineBreaks = lineBreakCount - existingLineBreaks

                if (neededLineBreaks > 0) {
                    // Agregar line breaks faltantes
                    repeat(neededLineBreaks) {
                        tokens = tokens.addAt(
                            Token(lineBreak, "\n", Position(0, 0)),
                            i
                        )
                        i++
                    }
                } else if (neededLineBreaks < 0) {
                    // Remover line breaks excesivos
                    var toRemove = -neededLineBreaks
                    j = i - 1

                    // Saltar espacios
                    while (j >= 0 && tokens.get(j)?.type == space) {
                        j--
                    }

                    // Remover line breaks
                    while (toRemove > 0 && j >= 0) {
                        if (tokens.get(j)?.type == lineBreak) {
                            val response = tokens.remove(j)
                            tokens = response.container
                            i--
                            toRemove--
                        }
                        j--
                    }
                }
            }
            i++
        }

        return tokens
    }
}
