package token.src.main.kotlin

import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

data class Token(
    val type: DataType?,
    val content: String,
    val position: Position
)
