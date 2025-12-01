package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object Literal : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        return when (node.type) {
            tokendata.src.main.kotlin.DataType.NUMBER_LITERAL -> {
                if (node.content.contains(".")) {
                    node.content.toDouble()
                } else {
                    node.content.toInt()
                }
            }
            tokendata.src.main.kotlin.DataType.STRING_LITERAL -> node.content
            tokendata.src.main.kotlin.DataType.BOOLEAN_LITERAL -> node.content.toBoolean()
            tokendata.src.main.kotlin.DataType.IDENTIFIER -> interpreter.resolveVariable(node.content)
                ?: throw IllegalStateException("Variable '${node.content}' not found")
            else -> throw IllegalArgumentException("Invalid literal type: ${node.type}")
        }
    }
}
