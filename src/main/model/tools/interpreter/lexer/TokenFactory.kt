package src.main.tools.interpreter.lexer

import src.main.structure.Container
import src.main.structure.Token
import src.main.structure.TokenMap
import src.main.structure.TokenPattern

object TokenFactory {
    fun createTokens(pieces: List<String>): Container {
        val container = Container() //se ejecuta cada vez  que lo llamo, no es un container para siempre
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