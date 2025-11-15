package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class IfStatement : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val condition = interpreter.interpret(node.children[0])

        return if (condition == true) {
            interpreter.interpret(node.children[1]) ?: Unit
        } else if (node.children.size > 2) {
            interpreter.interpret(node.children[2]) ?: Unit
        } else {
            Unit
        }
    }
}
