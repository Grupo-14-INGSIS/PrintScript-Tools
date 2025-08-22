package src.main.model.tools.interpreter.lexer

import common.src.main.kotlin.Container
import common.src.main.kotlin.Token
import common.src.main.kotlin.TokenMap
import common.src.main.kotlin.TokenPattern

object TokenFactory {
    fun createTokens(pieces: List<String>): Container {
        val container = Container() // se ejecuta cada vez  que lo llamo, no es un container para siempre
        var position = 0

        pieces.filter { it.isNotEmpty() }
            .forEach { piece ->
                val type = TokenMap.classifyTokenMap(piece)
                    ?: TokenPattern.classifyTokenPattern(piece)
                val token = Token(type, piece, position)
                container.addContainer(token)
                position++
            }

        return container
    }
}
