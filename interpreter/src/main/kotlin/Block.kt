package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class Block : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        var result: Any = Unit
        for (statement in node.children) {
            result = interpreter.interpret(statement) ?: Unit
        }
        return result
    }
}
