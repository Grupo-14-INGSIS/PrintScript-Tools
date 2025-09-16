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
        var i = 0
        var indentLevel = 0
        var atLineStart = true

        while (i < tokens.size()) {
            val token = tokens.get(
                i
            ) ?: break

            when {
                // Nueva línea - marcar que estamos al inicio
                token.type == lineBreak -> {
                    atLineStart = true
                }

                // Llave de cierre - reducir indentación
                token.type == closeBrace -> {
                    indentLevel = maxOf(
                        0,
                        indentLevel - 1
                    )
                    if (atLineStart) {
                        tokens = addIndentation(
                            tokens,
                            i,
                            indentLevel
                        )
                        atLineStart = false
                    }
                }

                // Llave de apertura - aumentar indentación para próxima línea
                token.type == openBrace -> {
                    if (atLineStart) {
                        tokens = addIndentation(
                            tokens,
                            i,
                            indentLevel
                        )
                        atLineStart = false
                    }
                    indentLevel++
                }

                // Cualquier otro token no-espacio al inicio de línea
                token.type != space && atLineStart -> {
                    tokens = addIndentation(
                        tokens,
                        i,
                        indentLevel
                    )
                    atLineStart = false
                }

                // Token de espacio al inicio de línea - remover indentación existente
                token.type == space && atLineStart -> {
                    // Remover espacios existentes al inicio de línea
                    var j = i
                    while (j < tokens.size() && tokens.get(
                            j
                        )?.type == space
                    ) {
                        val response = tokens.remove(
                            j
                        )
                        tokens = response.container
                        if (response.token == null) break
                    }
                    // Agregar indentación correcta
                    tokens = addIndentation(
                        tokens,
                        i,
                        indentLevel
                    )
                    atLineStart = false
                    continue // Saltar incremento de i
                }
            }

            i++
        }

        return tokens
    }

    private fun addIndentation(tokens: Container, index: Int, level: Int): Container {
        val indentation = " ".repeat(indentSize * level)
        return if (indentation.isNotEmpty()) {
            tokens.addAt(
                Token(
                    space,
                    indentation,
                    Position(
                        0,
                        0
                    )
                ),
                index
            )
        } else {
            tokens
        }
    }
}
