package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

object AssignmentToExistingVar : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val variableName = node.children[0].content
        val rawValue = interpreter.interpret(node.children[1])

        val expectedType = interpreter.resolveVariableType(variableName)
            ?: throw IllegalStateException("Could not resolve type for variable '$variableName'")

        val coercedValue = coerceValue(rawValue, expectedType)

        validateTypeCompatibility(coercedValue, expectedType)

        interpreter.assignVariable(variableName, coercedValue)

        return coercedValue ?: Unit
    }

    private fun coerceValue(value: Any?, expectedType: String): Any? {
        if (value !is String) return value

        return when (expectedType.lowercase()) {
            "number" -> value.toDoubleOrNull()
                ?: throw IllegalArgumentException("Valor '$value' no se puede convertir a número")
            "boolean" -> when (value.lowercase()) {
                "true" -> true
                "false" -> false
                else -> throw IllegalArgumentException("Valor '$value' no se puede convertir a booleano")
            }
            else -> value
        }
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
