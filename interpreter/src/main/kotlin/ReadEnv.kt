package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class ReadEnv(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode): Any {
        val varNameNode = node.children.firstOrNull() ?: throw IllegalArgumentException("readEnv requires a variable name argument")
        val varName = varNameNode.content.removeSurrounding("\"", "'")
        val envValue = inputProvider.readEnv(varName) ?: throw IllegalArgumentException("Environment variable '$varName' not found")

        return when {
            envValue.toBooleanStrictOrNull() != null -> envValue.toBoolean()
            envValue.toDoubleOrNull() != null -> envValue.toDouble()
            else -> envValue
        }
    }
}
