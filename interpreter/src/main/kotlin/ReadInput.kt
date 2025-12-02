package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class ReadInput(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        require(node.children.size <= 1) { "readInput expects 0 or 1 arguments" }

        val rawPrompt = if (node.children.isNotEmpty()) {
            interpreter.interpret(node.children[0])?.toString() ?: ""
        } else {
            ""
        }
        val prompt = rawPrompt.removeSurrounding("\"")

        return inputProvider.readInput(prompt)
    }
}
