package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object ConstDeclarationAndAssignment : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        require(node.children.size >= 3) { "Declaración de constante con asignación inválida: faltan argumentos" }

        val constantName = node.children[0].content // "x"
        val constantType = node.children[1].content // "number"
        val assignedValue = interpreter.interpret(node.children[2])

        // validar que el valor sea compatible con el tipo
        validateTypeCompatibility(
            assignedValue,
            constantType
        )

        interpreter.declareConstant(constantName, assignedValue, constantType)

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
