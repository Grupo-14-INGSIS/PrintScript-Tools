package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object Divide : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val left = interpreter.interpret(node.children[0])
        val right = interpreter.interpret(node.children[1])

        val leftDouble = (left as? Number)?.toDouble() ?: throw IllegalArgumentException(
            "Cannot divide with non-numeric left operand: ${left?.javaClass?.simpleName}"
        )
        val rightDouble = (right as? Number)?.toDouble() ?: throw IllegalArgumentException(
            "Cannot divide with non-numeric right operand: ${right?.javaClass?.simpleName}"
        )

        if (rightDouble == 0.0) {
            throw IllegalArgumentException("Cannot divide by zero")
        }

        return leftDouble / rightDouble
    }
}
