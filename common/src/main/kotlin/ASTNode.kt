package common.src.main.kotlin

class ASTNode (
    val token: Token,
    val children: List<ASTNode>
)