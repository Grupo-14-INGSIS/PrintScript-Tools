package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object AssignmentToExistingVar : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val variableName = node.children[0].content
        val newValue = interpreter.interpret(node.children[1])

        val expectedType = interpreter.resolveVariableType(variableName)
            ?: throw IllegalStateException("Could not resolve type for variable '$variableName'")

        validateTypeCompatibility(newValue, expectedType)

        interpreter.assignVariable(variableName, newValue)

        return newValue ?: Unit
    }

    private fun validateTypeCompatibility(value: Any?, expectedType: String) {
        if (expectedType.isEmpty()) return

        val isValid = when (expectedType.lowercase()) {
            "number" -> value is Number
            "string" -> value is String
            "boolean" -> value is Boolean
            else -> true
        }

        if (!isValid) {
            throw IllegalArgumentException(
                "Value $value is not compatible with type '$expectedType'"
            )
        }
    }
}
