package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class IfStatement : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val conditionResult = interpreter.interpret(node.children[0])

        if (conditionResult !is Boolean) {
            throw IllegalStateException(
                "La condición de un 'if' debe ser booleana, pero se encontró: ${conditionResult?.javaClass?.simpleName}"
            )
        }

        return if (conditionResult) { // Simplificado de '== true'
            interpreter.interpret(node.children[1]) ?: Unit
        } else if (node.children.size > 2) {
            // Existe un bloque 'else'
            interpreter.interpret(node.children[2]) ?: Unit
        } else {
            // No existe un bloque 'else'
            Unit
        }
    }
}
