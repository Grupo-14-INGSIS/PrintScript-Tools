package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class IfBraceOnSameLineRule : FormatRule {
    override fun format(source: Container): Container {
        val newTokens = mutableListOf<Token>()
        var i = 0
        while (i < source.size()) {
            val currentToken = source.get(i)!!

            if (currentToken.type == DataType.IF_KEYWORD) {
                var closingParenIndex = -1
                var openBraceIndex = -1

                // Encontrar el ')' del if
                for (j in i + 1 until source.size()) {
                    if (source.get(j)!!.type == DataType.CLOSE_PARENTHESIS) {
                        closingParenIndex = j
                        break
                    }
                }

                if (closingParenIndex != -1) {
                    // Buscar el '{' que le sigue, saltando espacios y saltos de línea
                    var nextTokenIndex = closingParenIndex + 1
                    while (nextTokenIndex < source.size() &&
                        (source.get(nextTokenIndex)!!.type == DataType.SPACE || source.get(nextTokenIndex)!!.type == DataType.LINE_BREAK)
                    ) {
                        nextTokenIndex++
                    }

                    if (nextTokenIndex < source.size() && source.get(nextTokenIndex)!!.type == DataType.OPEN_BRACE) {
                        openBraceIndex = nextTokenIndex
                    }
                }

                if (openBraceIndex != -1) {
                    // Añadir todos los tokens desde 'if' hasta ')'
                    for (j in i..closingParenIndex) {
                        newTokens.add(source.get(j)!!)
                    }
                    // Añadir un espacio
                    newTokens.add(Token(DataType.SPACE, " ", currentToken.position))
                    // Añadir la llave
                    newTokens.add(source.get(openBraceIndex)!!)

                    // Avanzar el índice principal
                    i = openBraceIndex
                } else {
                    newTokens.add(currentToken)
                }
            } else {
                newTokens.add(currentToken)
            }
            i++
        }
        return Container(newTokens)
    }
}
