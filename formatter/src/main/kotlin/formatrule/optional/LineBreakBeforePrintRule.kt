package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class LineBreakBeforePrintRule(private val lineBreakCount: Int) : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val lineBreak = DataType.LINE_BREAK

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(
                i
            ) ?: break

            // Buscar "println"
            if (token.type == identifier && token.content == "println") {
                // Contar saltos de línea existentes antes del println
                var existingLineBreaks = 0
                var j = i - 1

                while (j >= 0) {
                    val prevToken = tokens.get(
                        j
                    ) ?: break
                    if (prevToken.type == lineBreak) {
                        existingLineBreaks++
                        j--
                    } else {
                        break
                    }
                }

                // Ajustar cantidad de saltos de línea
                val neededLineBreaks = lineBreakCount - existingLineBreaks

                if (neededLineBreaks > 0) {
                    // Agregar saltos de línea faltantes
                    repeat(
                        neededLineBreaks
                    ) {
                        tokens = tokens.addAt(
                            Token(
                                lineBreak,
                                "\n",
                                Position(
                                    0,
                                    0
                                )
                            ),
                            i
                        )
                        i++ // Ajustar índice porque agregamos tokens
                    }
                } else if (neededLineBreaks < 0) {
                    // Remover saltos de línea excesivos
                    repeat(
                        -neededLineBreaks
                    ) {
                        if (i > 0 && tokens.get(
                                i - 1
                            )?.type == lineBreak
                        ) {
                            val response = tokens.remove(
                                i - 1
                            )
                            tokens = response.container
                            i-- // Ajustar índice porque removemos tokens
                        }
                    }
                }
            }
            i++
        }

        return tokens
    }
}
