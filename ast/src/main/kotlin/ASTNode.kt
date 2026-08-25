package ast.src.main.kotlin


class ASTNode(
    val type: ASTNodeType?,
    val content: String,
    val position: Position,
    val children: List<ASTNode>
) {
    override fun toString(): String {
        return toString(0)
    }

    private fun toString(indent: Int): String {
        val sb = StringBuilder()
        sb.append("  ".repeat(indent))
        sb.append("ASTNode(type=$type, content='$content'")
        if (children.isNotEmpty()) {
            sb.append(", children=[\n")
            for (child in children) {
                sb.append(child.toString(indent + 1))
            }
            sb.append("  ".repeat(indent))
            sb.append("]")
        }
        sb.append(")\n")
        return sb.toString()
    }
}
