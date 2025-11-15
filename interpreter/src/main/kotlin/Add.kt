package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object Add : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val left = interpreter.interpret(node.children[0])
        val right = interpreter.interpret(node.children[1])

        return when {
            left is String || right is String -> left.toString() + right.toString()
            left is Double || right is Double -> left.toString().toDouble() + right.toString().toDouble()
            left is Int && right is Int -> left + right
            else -> throw IllegalArgumentException("Cannot add types ${left?.javaClass?.simpleName} and ${right?.javaClass?.simpleName}")
        }
    }
}

// cubre chaining y mixed chaining
