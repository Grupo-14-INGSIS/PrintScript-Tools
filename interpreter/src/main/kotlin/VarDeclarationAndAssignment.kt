package src.main.model.tools.interpreter.interpreter

import src.main.model.structure.ASTNode

object VarDeclarationAndAssignment : ActionType {
    private val variables = mutableMapOf<String, Any?>()

    override fun interpret(node: ASTNode, actions: src.main.model.tools.interpreter.interpreter.Actions): Any {

        require(node.children.size >= 3) { "Declaración con asignación inválida: faltan argumentos" }

        val variableName = node.children[0].token.content  // "x"
        val variableType = node.children[1].token.content  // "number"
        val assignedValue = node.children[2].token.content // "5"

        // evitar redeclaracion
        if (src.main.model.tools.interpreter.interpreter.VarDeclarationAndAssignment.variables.containsKey(variableName)) {
            throw IllegalStateException("La variable '$variableName' ya fue declarada")
        }

        // convertir el valor asignado al tipo correcto
        val valueToStore = src.main.model.tools.interpreter.interpreter.VarDeclarationAndAssignment.convertValueToType(
            assignedValue,
            variableType
        )

        // validar que el valor sea compatible con el tipo
        src.main.model.tools.interpreter.interpreter.VarDeclarationAndAssignment.validateTypeCompatibility(
            valueToStore,
            variableType
        )

        src.main.model.tools.interpreter.interpreter.VarDeclarationAndAssignment.variables[variableName] = valueToStore

        return Unit
    }

    private fun convertValueToType(value: String, expectedType: String): Any? {
        return when (expectedType.lowercase()) {
            "number" -> value.toDoubleOrNull() ?: value.toIntOrNull()
            "string" -> value
            "boolean" -> value.toBooleanStrictOrNull()
            else -> value
        }
    }

    private fun validateTypeCompatibility(value: Any?, expectedType: String) {
        val isValid = when (expectedType.lowercase()) {
            "number" -> value is Number
            "string" -> value is String
            "boolean" -> value is Boolean
            else -> true
        }

        if (!isValid) {
            throw IllegalArgumentException("Valor '$value' no es compatible con tipo '$expectedType'")
        }
    }
}