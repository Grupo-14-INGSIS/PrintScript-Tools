package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object Divide : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val left = interpreter.interpret(node.children[0])
        val right = interpreter.interpret(node.children[1])

        if (right.toString().toDouble() == 0.0) {
            throw IllegalArgumentException("Cannot divide by zero")
        }

        return when {
            left is Number && right is Number -> left.toString().toDouble() / right.toString().toDouble()
            else -> throw IllegalArgumentException("Cannot divide types ${left?.javaClass?.simpleName} and ${right?.javaClass?.simpleName}")
        }
    }
}
