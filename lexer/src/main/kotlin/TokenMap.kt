package lexer.src.main.kotlin

import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

object TokenMap {

    fun classifyTokenMap(piece: String, version: String = "1.0"): DataType? {
        val plugins = TokenPluginFactory.createPlugins(version)
        return plugins.filterIsInstance<ExactMatchTokenPlugin>()
            .firstNotNullOfOrNull { it.match(piece, Position(0, 0))?.type }
    }
}
