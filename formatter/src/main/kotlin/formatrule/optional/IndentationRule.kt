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

        for (i in 0 until source.size()) {
            val currentToken = source.get(i)!!

            if (currentToken.type == DataType.OPEN_BRACE) {
                newTokens.add(currentToken)
                indentationLevel++
            } else if (currentToken.type == DataType.CLOSE_BRACE) {
                indentationLevel--
                // Si la llave de cierre está en una nueva línea, aplicar indentación
                if (newTokens.lastOrNull()?.type == DataType.LINE_BREAK) {
                    newTokens.add(Token(DataType.SPACE, indent.repeat(indentationLevel), currentToken.position))
                }
                newTokens.add(currentToken)
            } else if (currentToken.type == DataType.LINE_BREAK) {
                newTokens.add(currentToken)
                // Mirar si el siguiente token no es una llave de cierre
                // Corrected: Check bounds before accessing
                if (i + 1 < source.size()) { // Check if there's a next token
                    val nextToken = source.get(i + 1)!! // Now it's safe to get
                    if (nextToken.type != DataType.CLOSE_BRACE) {
                        newTokens.add(Token(DataType.SPACE, indent.repeat(indentationLevel), currentToken.position))
                    }
                } else {
                    // If it's the last token and it's a LINE_BREAK, no indentation needed after it
                }
            } else {
                newTokens.add(currentToken)
            }
        }
        return Container(newTokens)
    }
}
