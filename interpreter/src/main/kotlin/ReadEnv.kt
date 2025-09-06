import common.src.main.kotlin.ASTNode
import src.main.model.tools.interpreter.interpreter.ActionType
import src.main.model.tools.interpreter.interpreter.Actions

class ReadEnv(private val inputProvider: InputProvider) : ActionType {
    override fun interpret(node: ASTNode, actions: Actions): Any {
        val varNameNode = node.children.firstOrNull() ?: throw IllegalArgumentException("readEnv requires a variable name argument")
        val varName = varNameNode.token.content.removeSurrounding("\"", "'")
        val envValue = inputProvider.readEnv(varName) ?: throw IllegalArgumentException("Environment variable '$varName' not found")

        return when {
            envValue.toBooleanStrictOrNull() != null -> envValue.toBoolean()
            envValue.toDoubleOrNull() != null -> envValue.toDouble()
            else -> envValue
        }
    }
}
