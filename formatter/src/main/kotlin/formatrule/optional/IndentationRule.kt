package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.formatrule.FormatRule
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType

class IndentationRule(private val indentSize: Int) : FormatRule {
    override fun format(source: Container): Container {
        val newTokens = mutableListOf<Token>()
        var indentationLevel = 0
        val indent = " ".repeat(indentSize)
        var isAtLineStart = true // Empezamos asumiendo que estamos al inicio de una línea

        for (i in 0 until source.size()) {
            val currentToken = source.get(i)!!

            if (isAtLineStart) {
                // Si estamos al inicio de una línea, ignoramos cualquier token de espacio.
                if (currentToken.type == DataType.SPACE) {
                    continue
                }

                // Encontramos el primer token que no es un espacio.
                // Añadimos la indentación correcta ANTES de este token.
                isAtLineStart = false
                val levelForCurrentLine = if (currentToken.type == DataType.CLOSE_BRACE) {
                    (indentationLevel - 1).coerceAtLeast(0)
                } else {
                    indentationLevel
                }

                if (levelForCurrentLine > 0) {
                    newTokens.add(Token(DataType.SPACE, indent.repeat(levelForCurrentLine), currentToken.position))
                }
            }

            newTokens.add(currentToken)

            when (currentToken.type) {
                DataType.OPEN_BRACE -> indentationLevel++
                DataType.CLOSE_BRACE -> indentationLevel = (indentationLevel - 1).coerceAtLeast(0)
                DataType.LINE_BREAK -> isAtLineStart = true
                else -> { /* No hacer nada */ }
            }
        }
        return Container(newTokens)
    }
}
