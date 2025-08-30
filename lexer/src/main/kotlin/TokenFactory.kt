package src.main.model.tools.interpreter.lexer

import common.src.main.kotlin.Container
import common.src.main.kotlin.Token
import common.src.main.kotlin.TokenMap
import common.src.main.kotlin.TokenPattern
import common.src.main.kotlin.Position

object TokenFactory {
    fun createTokens(pieces: List<String>): Container {
        val container = Container()
        var position = Position(line = 0, column = 0)

        pieces.filter { it.isNotEmpty() }
            .forEach { piece ->
                val type = TokenMap.classifyTokenMap(piece)
                    ?: TokenPattern.classifyTokenPattern(piece)

                val token = Token(type, piece, position)
                container.addContainer(token)

                // Actualizar posición
                val lines = piece.split("\n")
                if (lines.size > 1) {
                    // Si hay saltos de línea, actualizamos línea y columna
                    position = Position(
                        line = position.line + lines.size - 1,
                        column = lines.last().length
                    )
                } else {
                    // Sino, solo incrementamos la columna
                    position = Position(
                        line = position.line,
                        column = position.column + piece.length
                    )
                }
            }

        return container
    }
}

