package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IfBraceBelowLineRule : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val openBrace = DataType.OPEN_BRACE
    private val closeParen = DataType.CLOSE_PARENTHESIS
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            // Buscar "if"
            if (token.type == identifier && token.content == "if") {
                // Buscar el cierre de paréntesis correspondiente
                var j = i + 1
                var parenCount = 0
                var closeParenIndex = -1

                while (j < tokens.size()) {
                    val currentToken = tokens.get(j) ?: break
                    val type = currentToken.type

                    if (type == DataType.OPEN_PARENTHESIS) {
                        parenCount++
                    } else if (type == closeParen) {
                        parenCount--
                        if (parenCount == 0) {
                            closeParenIndex = j
                            break
                        }
                    }
                    j++
                }

                if (closeParenIndex != -1) {
                    // Buscar la llave de apertura después del cierre de paréntesis
                    var braceIndex = -1
                    j = closeParenIndex + 1

                    while (j < tokens.size()) {
                        val currentToken = tokens.get(j) ?: break
                        when (currentToken.type) {
                            openBrace -> {
                                braceIndex = j
                                break
                            }
                            space, lineBreak -> {
                                // Continuar buscando
                            }
                            else -> {
                                // Otro token antes de la llave
                                break
                            }
                        }
                        j++
                    }

                    if (braceIndex != -1) {
                        // Remover todos los espacios y saltos de línea entre ) y {
                        var k = closeParenIndex + 1
                        while (k < braceIndex) {
                            val tokenToCheck = tokens.get(k) ?: break
                            if (tokenToCheck.type == lineBreak || tokenToCheck.type == space) {
                                val response = tokens.remove(k)
                                tokens = response.container
                                if (response.token == null) break
                                braceIndex--
                            } else {
                                k++
                            }
                        }

                        // Agregar exactamente un salto de línea entre ) y {
                        tokens = tokens.addAt(
                            Token(
                                lineBreak,
                                "\n",
                                Position(0, 0)
                            ),
                            closeParenIndex + 1
                        )
                    }
                }
            }
            i++
        }

        return tokens
    }
}
