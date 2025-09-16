package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object Literal : ActionType {
    override fun interpret(node: ASTNode): Any {
        // It's a boolean
        if (node.content.toBoolean()) {
            return node.content.toBoolean()
        } else {
            try {
                // It's a float
                val output = node.content.toFloat()
                return output
            } catch (e: NumberFormatException) {
                // Neither boolean nor float -> it's a string
                return node.content
            }
        }
    }
}
