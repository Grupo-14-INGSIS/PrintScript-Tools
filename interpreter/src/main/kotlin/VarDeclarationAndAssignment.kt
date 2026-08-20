package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType

object VarDeclarationAndAssignment : ActionType {
    override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
        require(node.children.size == 2) { "Invalid declaration structure" }

        val declarationNode = node.children[0]
        val valueNode = node.children[1]

        require(declarationNode.children.size == 2) { "Invalid declaration node structure" }

        val variableName = declarationNode.children[0].content
        val variableType = declarationNode.children[1].content
        val rawValue = interpreter.interpret(valueNode)

        val coercedValue = coerceValue(rawValue, variableType)

        validateTypeCompatibility(coercedValue, variableType)

        if (declarationNode.type == ASTNodeType.CONST_KEYWORD) {
            interpreter.declareConstant(variableName, coercedValue, variableType)
        } else {
            interpreter.declareVariable(variableName, coercedValue, variableType)
        }

        return Unit
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
        val isValid = when (expectedType.lowercase()) {
            "number" -> value is Number
            "string" -> value is String
            "boolean" -> value is Boolean
            else -> true
        }

        if (!isValid) {
            throw IllegalArgumentException(
                "Valor '$value' no es compatible con tipo '$expectedType'"
            )
        }
    }
}
