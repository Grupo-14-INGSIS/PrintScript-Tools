package Lexer

import model.structure.Container
import model.structure.Token
import model.structure.TokenMap
import model.structure.TokenPattern

class TokenFactory {
    fun createTokens(pieces: List<String>): Container {
        val container = Container()
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