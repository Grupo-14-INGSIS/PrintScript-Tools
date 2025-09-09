package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class IfStatement(private val interpreter: Interpreter) : ActionType {
    override fun interpret(node: ASTNode): Any {
        val condition = node.children[0]
        val thenBlock = node.children[1]
        val elseBlock = node.children.getOrNull(2)

        val conditionAction = interpreter.determineAction(condition)
        val conditionResult = interpreter.interpret(condition, conditionAction)

        return if (conditionResult == true) {
            val thenAction = interpreter.determineAction(thenBlock)
            interpreter.interpret(thenBlock, thenAction) ?: Unit
        } else if (elseBlock != null) {
            val elseAction = interpreter.determineAction(elseBlock)
            interpreter.interpret(elseBlock, elseAction) ?: Unit
        } else {
            Unit
        }
    }
}
