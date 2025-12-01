package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object VarDeclaration : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        require(node.children.size >= 2) { "Declaración inválida: faltan argumentos" }

        val variableName = node.children[0].content
        val variableType = node.children[1].content

        interpreter.declareVariable(variableName, defaultValueForType(variableType), variableType)

        return Unit
    }

    private fun defaultValueForType(type: String): Any? =
        when (type.lowercase()) {
            "number" -> 0.0
            "string" -> ""
            "boolean" -> false
            else -> null
        }
}
