package src.main.model.structure

class ASTNode (
    val token: Token,
    val children: List<ASTNode>
)