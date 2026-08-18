package lexer.src.main.kotlin

import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

/**
 * Plugin de tokenización que evalúa expresiones regulares para construir tokens
 * (útil para literales de string, numéricos, identificadores, etc.).
 */
class RegexTokenPlugin(
    private val regex: Regex,
    private val type: DataType
) : TokenPlugin {

    override fun match(piece: String, position: Position): Token? {
        if (piece.matches(regex)) {
            return Token(type, piece, position)
        }
        return null
    }

    fun getRegex(): Regex = regex
    fun getDataType(): DataType = type
}
