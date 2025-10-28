package formatter.src.main.kotlin.formatrule.optional

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token
import formatter.src.main.kotlin.formatrule.FormatRule

class IfBraceBelowLineRule : FormatRule {

    private val identifier = DataType.IDENTIFIER
    private val openBrace = DataType.OPEN_BRACE
    private val closeBrace = DataType.CLOSE_BRACE
    private val closeParen = DataType.CLOSE_PARENTHESIS
    private val lineBreak = DataType.LINE_BREAK
    private val space = DataType.SPACE
    private val semicolon = DataType.SEMICOLON
    private val openParen = DataType.OPEN_PARENTHESIS

    override fun format(source: Container): Container {
        var tokens = source
        var i = 0

        while (i < tokens.size()) {
            val token = tokens.get(i) ?: break

            if (token.type == identifier && token.content == "if") {
                val closeParenIndex = findCloseParenthesis(tokens, i)
                if (closeParenIndex == -1) {
                    i++
                    continue
                }

                val openBraceIndex = findOpenBrace(tokens, closeParenIndex)

                if (openBraceIndex != -1) {
                    // 1. Ya tiene llave, moverla a la línea siguiente.

                    // **Lógica de limpieza mejorada (tomada del segundo código):**
                    // Remover todo entre ')' y '{'
                    tokens = removeBetweenParenAndBrace(tokens, closeParenIndex, openBraceIndex)

                    // El nuevo índice de la llave de apertura es ahora 'closeParenIndex + 1'
                    val newOpenBraceIndex = closeParenIndex + 1

                    // **Asegurar un solo salto de línea:**
                    tokens = tokens.addAt(
                        Token(lineBreak, "\n", Position(0, 0)),
                        newOpenBraceIndex
                    )
                } else {
                    // 2. No tiene llave, insertarla (Lógica de inserción del primer código).

                    val insertOpenIndex = closeParenIndex + 1

                    // Limpiar posibles espacios/saltos de línea justo después del ')'
                    tokens = removeSpacesAndLineBreaks(tokens, insertOpenIndex)

                    // Insertar salto de línea y llave de apertura
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), insertOpenIndex)
                    tokens = tokens.addAt(Token(openBrace, "{", Position(0, 0)), insertOpenIndex + 1)
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), insertOpenIndex + 2)


                    // Buscar fin de la instrucción (hasta el punto y coma)
                    var endIndex = insertOpenIndex + 4
                    while (endIndex < tokens.size()) {
                        val t = tokens.get(endIndex)
                        if (t?.type == semicolon) break
                        endIndex++
                    }

                    // Insertar salto de línea y llave de cierre
                    tokens = tokens.addAt(Token(lineBreak, "\n", Position(0, 0)), endIndex + 1)
                    tokens = tokens.addAt(Token(closeBrace, "}", Position(0, 0)), endIndex + 2)
                }
            }

            i++
        }

        // Limpieza final: quitar saltos de línea al final del archivo
        while (tokens.size() > 0 && tokens.get(tokens.size() - 1)?.type == lineBreak) {
            val response = tokens.remove(tokens.size() - 1)
            tokens = response.container
        }

        return tokens
    }

    /**
     * Agrega indentación (4 espacios) a la línea después del '{' (Del primer código original).
     */
    private fun addIndentationInsideIf(tokens: Container, openBraceIndex: Int): Container {
        var result = tokens
        var j = openBraceIndex

        // Buscar el primer salto de línea después del '{'
        while (j < result.size()) {
            val t = result.get(j)
            if (t?.type == lineBreak) {
                val next = result.get(j + 1)
                if (next != null && next.type != space && next.type != closeBrace) {
                    result = result.addAt(Token(space, "    ", Position(0, 0)), j + 1)
                }
                break
            }
            j++
        }
        return result
    }

    /**
     * Encuentra el paréntesis de cierre ')' que balancea el 'if'.
     */
    private fun findCloseParenthesis(tokens: Container, startIndex: Int): Int {
        var parenCount = 0
        var j = startIndex + 1
        while (j < tokens.size()) {
            val currentToken = tokens.get(j) ?: break
            when (currentToken.type) {
                openParen -> parenCount++
                closeParen -> {
                    parenCount--
                    if (parenCount == 0) return j
                }
                else -> {}
            }
            j++
        }
        return -1
    }

    /**
     * Busca la llave de apertura '{' inmediatamente después del paréntesis de cierre,
     * ignorando espacios y saltos de línea (Del segundo código).
     */
    private fun findOpenBrace(tokens: Container, startIndex: Int): Int {
        var j = startIndex + 1
        while (j < tokens.size()) {
            val currentToken = tokens.get(j) ?: break
            when (currentToken.type) {
                openBrace -> return j
                space, lineBreak -> j++
                else -> return -1 // Si encuentra cualquier otra cosa antes de la llave, no hay llave
            }
        }
        return -1
    }

    /**
     * Elimina todos los espacios y saltos de línea entre el paréntesis de cierre ')'
     * y la llave de apertura '{'. (El corazón de la mejora del segundo código).
     */
    private fun removeBetweenParenAndBrace(tokens: Container, closeParenIndex: Int, openBraceIndex: Int): Container {
        var result = tokens
        var currentBraceIndex = openBraceIndex

        // El bucle se mantiene mientras haya tokens entre ')' y '{'
        while (closeParenIndex + 1 < currentBraceIndex) {
            val tokenBetween = result.get(closeParenIndex + 1)

            if (tokenBetween?.type == lineBreak || tokenBetween?.type == space) {
                val response = result.remove(closeParenIndex + 1)
                result = response.container
                if (response.token == null) break // Seguridad si la remoción falla
                currentBraceIndex-- // La posición de la llave se ha movido un paso a la izquierda
            } else {
                break // Encontró algo que no es espacio/salto de línea
            }
        }
        return result
    }

    /**
     * Elimina espacios y saltos de línea desde un índice dado hacia adelante hasta encontrar
     * algo diferente. (Del primer código, usado para el caso de inserción).
     */
    private fun removeSpacesAndLineBreaks(tokens: Container, startIndex: Int): Container {
        var result = tokens
        var j = startIndex
        while (j < result.size()) {
            val t = result.get(j)
            if (t?.type == space || t?.type == lineBreak) {
                val response = result.remove(j)
                result = response.container
            } else {
                break
            }
        }
        return result
    }
}
