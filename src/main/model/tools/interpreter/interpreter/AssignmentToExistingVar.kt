package src.main.tools.interpreter.interpreter

import src.main.structure.ASTNode

object AssignmentToExistingVar : ActionType {
    private val variables = mutableMapOf<String, Any>() //

    override fun interpret(node: ASTNode, action: Actions) : Any {
        val variableName = node.children[0].token.content  // nombre variable
        val newValue = node.children[1].token.content      // valor variable

        val valueToStore = newValue.toDoubleOrNull() ?: newValue

        variables[variableName] = valueToStore

        return valueToStore
    }
}