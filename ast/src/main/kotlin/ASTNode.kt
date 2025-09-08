package ast.src.main.kotlin

import tokendata.src.main.kotlin.Position
import tokendata.src.main.kotlin.DataType

class ASTNode(
    val type: DataType?,
    val content: String,
    val position: Position,
    val children: List<ASTNode>
)
