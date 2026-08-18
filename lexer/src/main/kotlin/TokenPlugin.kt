package lexer.src.main.kotlin

import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.Position

/**
 * Abstracción de plugin para el reconocimiento y construcción de Tokens.
 * Permite extender el Core Lexer con nuevas reglas léxicas sin modificar su código base.
 */
interface TokenPlugin {
    /**
     * Intenta hacer match de una pieza o lexema de texto en una posición determinada.
     * @param piece El lexema o texto a clasificar.
     * @param position La posición (línea, columna) del token.
     * @return El Token construido si el plugin reconoce la pieza, o null si no aplica.
     */
    fun match(piece: String, position: Position): Token?
}
