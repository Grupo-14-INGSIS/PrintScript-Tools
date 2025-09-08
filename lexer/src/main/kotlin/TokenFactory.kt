package src.main.model.tools.interpreter.lexer

import tokendata.src.main.kotlin.DataType
import container.src.main.kotlin.Container
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.Position

object TokenFactory {
    fun createTokens(pieces: List<String>, version: String = "1.0"): Container {
        val container = Container()
        var position = Position(line = 0, column = 0)

        pieces.filter { it.isNotEmpty() }
            .forEach { piece ->
                val type = when (piece) {
                    " ", "\t", "\r", "\n" -> DataType.SPACE
                    else -> TokenMap.classifyTokenMap(piece, version)
                        ?: TokenPattern.classifyTokenPattern(piece)
                }

                val token = Token(type, piece, position)
                container.addContainer(token)

                // Actualizar posición
                val lines = piece.split("\n")
                position = if (lines.size > 1) {
                    Position(
                        line = position.line + lines.size - 1,
                        column = lines.last().length
                    )
                } else {
                    Position(
                        line = position.line,
                        column = position.column + piece.length
                    )
                }
            }

        return container
    }
}
