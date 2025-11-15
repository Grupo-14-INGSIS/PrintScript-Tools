package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class ReadEnv(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val varName = interpreter.interpret(node.children[0]).toString()
        val envValue = inputProvider.readEnv(varName) ?: throw IllegalArgumentException("Environment variable '$varName' not found")

        return when {
            envValue.toBooleanStrictOrNull() != null -> envValue.toBoolean()
            envValue.toDoubleOrNull() != null -> envValue.toDouble()
            else -> envValue
        }
    }
}
