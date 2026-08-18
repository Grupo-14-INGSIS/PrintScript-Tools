package lexer.src.main.kotlin

import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

object TokenPattern {

    fun classifyTokenPattern(piece: String): DataType? {
        val plugins = TokenPluginFactory.createPlugins("1.0")
        return plugins.filterIsInstance<RegexTokenPlugin>()
            .firstNotNullOfOrNull { it.match(piece, Position(0, 0))?.type }
    }
}
