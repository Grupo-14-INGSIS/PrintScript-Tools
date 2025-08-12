package src.main.structure

class ASTNode (
    val token: Token,
    val children: List<ASTNode>
)