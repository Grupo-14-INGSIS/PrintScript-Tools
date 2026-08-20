package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType

object Literal : ActionType {
    override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
        return when (node.type) {
            ASTNodeType.NUMBER_LITERAL -> {
                if (node.content.contains(".")) {
                    node.content.toDouble()
                } else {
                    node.content.toInt()
                }
            }
            ASTNodeType.STRING_LITERAL -> node.content
            ASTNodeType.BOOLEAN_LITERAL -> node.content.toBoolean()
            ASTNodeType.IDENTIFIER -> interpreter.resolveVariable(node.content) ?: ""
            else -> throw IllegalArgumentException("Invalid literal type: ${node.type}")
        }
    }
}
