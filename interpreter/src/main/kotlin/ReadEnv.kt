package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class ReadEnv(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        require(node.children.size == 1) { "readEnv expects 1 argument" }

        val rawVarName = interpreter.interpret(node.children[0])?.toString()
            ?: throw IllegalArgumentException("Environment variable name cannot be null")
        val varName = rawVarName.removeSurrounding("\"")

        return inputProvider.readEnv(varName)
            ?: throw IllegalArgumentException("Environment variable '$varName' not found")
    }
}
