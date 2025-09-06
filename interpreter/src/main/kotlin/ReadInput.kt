import common.src.main.kotlin.ASTNode
import src.main.model.tools.interpreter.interpreter.ActionType
import src.main.model.tools.interpreter.interpreter.Actions

class ReadInput(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, actions: Actions): Any {
        val promptNode = node.children.firstOrNull() ?: throw IllegalArgumentException("readInput requires a prompt argument")
        val prompt = promptNode.token.content.removeSurrounding("\"", "'")
        val input = inputProvider.readInput(prompt)
        return input.toDoubleOrNull() ?: input
    }
}
