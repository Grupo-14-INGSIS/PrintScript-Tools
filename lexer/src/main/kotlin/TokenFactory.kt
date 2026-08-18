package lexer.src.main.kotlin

import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.Position

object TokenFactory {

    fun createTokens(pieces: List<String>, plugins: List<TokenPlugin>): Container {
        var container = Container()
        var position = Position(line = 0, column = 0)

        pieces.filter { it.isNotEmpty() }
            .forEach { piece ->
                val token = plugins.firstNotNullOfOrNull { it.match(piece, position) }

                if (token != null) {
                    container = container.addContainer(token)

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
            }
        return container
    }

    fun createTokens(pieces: List<String>, version: String = "1.0"): Container {
        val plugins = TokenPluginFactory.createPlugins(version)
        return createTokens(pieces, plugins)
    }
}
