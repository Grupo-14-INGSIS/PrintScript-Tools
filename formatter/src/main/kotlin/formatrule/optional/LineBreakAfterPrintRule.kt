package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class LineBreakAfterPrintRule(private val lineBreaks: Int) : FormatRule {
    override fun format(tokens: Container): Container {
        val newTokens = mutableListOf<Token>()
        var i = 0
        while (i < tokens.size()) {
            val currentToken = tokens.get(i)!!
            newTokens.add(currentToken)

            if (currentToken.type == DataType.PRINTLN) {
                // Encontrar el punto y coma que cierra la sentencia println
                var semicolonIndex = -1
                for (j in i + 1 until tokens.size()) {
                    if (tokens.get(j)!!.type == DataType.SEMICOLON) {
                        semicolonIndex = j
                        break
                    }
                }

                if (semicolonIndex != -1) {
                    // Añadir los tokens hasta el punto y coma
                    for (j in i + 1..semicolonIndex) {
                        newTokens.add(tokens.get(j)!!)
                    }
                    i = semicolonIndex

                    // Verificar si es la última sentencia del archivo
                    var isLastStatement = true
                    for (k in semicolonIndex + 1 until tokens.size()) {
                        val nextType = tokens.get(k)!!.type
                        if (nextType != DataType.SPACE && nextType != DataType.LINE_BREAK) {
                            isLastStatement = false
                            break
                        }
                    }

                    // Si NO es la última sentencia, aplicar la lógica de saltos de línea
                    if (!isLastStatement) {
                        // Avanzar más allá de los saltos de línea existentes
                        var nextTokenIndex = semicolonIndex + 1
                        while (nextTokenIndex < tokens.size() && tokens.get(nextTokenIndex)!!.type == DataType.LINE_BREAK) {
                            nextTokenIndex++
                        }

                        // Añadir la cantidad correcta de saltos de línea (config + 1)
                        val breaksToAdd = lineBreaks + 1
                        for (k in 0 until breaksToAdd) {
                            newTokens.add(Token(DataType.LINE_BREAK, "\n", currentToken.position))
                        }

                        // Mover el índice principal al lugar correcto
                        i = nextTokenIndex - 1
                    } else {
                        // Si es la última sentencia, no se añaden saltos de línea y terminamos
                        i = tokens.size()
                    }
                }
            }
            i++
        }
        return Container(newTokens)
    }
}
