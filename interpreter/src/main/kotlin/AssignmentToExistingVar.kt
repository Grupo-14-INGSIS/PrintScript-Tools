package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object AssignmentToExistingVar : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val variableName = node.children[0].content // nombre variable
        val newValue = interpreter.interpret(node.children[1]) // valor variable

        interpreter.assignVariable(variableName, newValue)

        return newValue ?: Unit
    }
}
