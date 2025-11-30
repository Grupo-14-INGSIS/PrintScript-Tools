package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object VarDeclarationAndAssignment : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        require(node.children.size == 2) { "Invalid declaration structure" }

        val declarationNode = node.children[0]
        val valueNode = node.children[1]

        require(declarationNode.children.size == 2) { "Invalid declaration node structure" }

        val variableName = declarationNode.children[0].content
        val variableType = declarationNode.children[1].content
        val assignedValue = interpreter.interpret(valueNode)

        // validar que el valor sea compatible con el tipo
        validateTypeCompatibility(
            assignedValue,
            variableType
        )

        if (declarationNode.type == tokendata.src.main.kotlin.DataType.CONST_KEYWORD) {
            interpreter.declareConstant(variableName, assignedValue, variableType)
        } else {
            interpreter.declareVariable(variableName, assignedValue, variableType)
        }

        return Unit
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
