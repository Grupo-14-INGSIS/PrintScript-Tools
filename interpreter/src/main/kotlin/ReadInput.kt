package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class ReadInput(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode): Any {
        val promptNode = node.children.firstOrNull() ?: throw IllegalArgumentException("readInput requires a prompt argument")
        val prompt = promptNode.content.removeSurrounding("\"", "'")
        val input = inputProvider.readInput(prompt)
        return input.toDoubleOrNull() ?: input
    }
}
