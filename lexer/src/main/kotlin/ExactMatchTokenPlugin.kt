package lexer.src.main.kotlin

import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

/**
 * Plugin de tokenización que hace coincidencia exacta de cadenas con tipos de datos específicos
 * (útil para palabras clave, operadores, delimitadores y espacios).
 */
class ExactMatchTokenPlugin(private val mappings: Map<String, DataType>) : TokenPlugin {

    override fun match(piece: String, position: Position): Token? {
        val type = mappings[piece] ?: return null
        return Token(type, piece, position)
    }

    fun getMappings(): Map<String, DataType> = mappings
}
