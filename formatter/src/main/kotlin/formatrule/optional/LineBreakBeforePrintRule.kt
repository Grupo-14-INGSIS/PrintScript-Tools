package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakBeforePrintRule(private val count: Int = 1) : FormatRule {

    private val printlnType = DataType.PRINTLN
    private val lineBreak = DataType.LINE_BREAK

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0
        var previousWasPrintln = false

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == printlnType) {
                // Solo insertar salto si hubo otro println antes
                if (!previousWasPrintln) {
                    previousWasPrintln = true
                    i++
                    continue
                }

                // Contar cuántos LINE_BREAK hay antes
                var lineBreaksBefore = 0
                var j = i - 1
                while (j >= 0) {
                    val prev = tokens.get(j)
                    if (prev?.type == lineBreak) {
                        lineBreaksBefore++
                        j--
                    } else if (prev?.type == DataType.SPACE) {
                        j--
                    } else {
                        break
                    }
                }

                // Insertar los que faltan
                val missing = count - lineBreaksBefore
                repeat(missing.coerceAtLeast(0)) {
                    tokens = tokens.addAt(
                        Token(lineBreak, "\n", Position(0, 0)),
                        i
                    )
                    i++ // avanzar para no reinsertar en el mismo lugar
                }
            }

            i++
        }

        return tokens
    }
}
