package common.src.main.kotlin

class ASTNode(
    val type: DataType?,
    val content: String,
    val position: Position,
    val children: List<ASTNode>
)
