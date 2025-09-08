package src.main.model.tools.interpreter.interpreter

import ast.src.main.kotlin.ASTNode

object AssignmentToExistingVar : ActionType {
    private val variables = mutableMapOf<String, Any>() //

    override fun interpret(node: ASTNode): Any {
        val variableName = node.children[0].content // nombre variable
        val newValue = node.children[1].content // valor variable

        val valueToStore = newValue.toDoubleOrNull() ?: newValue

        variables[variableName] = valueToStore

        return valueToStore
    }
}
