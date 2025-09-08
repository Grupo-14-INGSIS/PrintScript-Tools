package src.main.model.tools.interpreter.interpreter

import ast.src.main.kotlin.ASTNode

object VarDeclaration : ActionType {
    private val variables = mutableMapOf<String, Any?>()

    override fun interpret(node: ASTNode, actions: Actions): Any {
        require(node.children.size >= 2) { "Declaración inválida: faltan argumentos" }

        val variableName = node.children[0].content
        val variableType = node.children[1].content

        // evitar redeclaracion
        if (variables.containsKey(variableName)) {
            throw IllegalStateException("La variable '$variableName' ya fue declarada")
        }

        // inicializa con valor default segun el tipo
        variables[variableName] = defaultValueForType(variableType)

        return Unit
    }

    private fun defaultValueForType(type: String): Any? =
        when (type.lowercase()) {
            "number" -> 0
            "string" -> ""
            "boolean" -> false
            else -> null
        }
}
