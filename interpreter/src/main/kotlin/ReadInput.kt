package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class ReadInput(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val prompt = interpreter.interpret(node.children[0])
        val input = inputProvider.readInput(prompt.toString())
        return input.toDoubleOrNull() ?: input
    }
}
