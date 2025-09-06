import common.src.main.kotlin.ASTNode
import src.main.model.tools.interpreter.interpreter.ActionType
import src.main.model.tools.interpreter.interpreter.Actions
import src.main.model.tools.interpreter.interpreter.Interpreter

class IfStatement(private val interpreter: Interpreter) : ActionType {
    override fun interpret(node: ASTNode, actions: Actions): Any {
        val condition = node.children[0]
        val thenBlock = node.children[1]
        val elseBlock = node.children.getOrNull(2)

        val conditionAction = interpreter.determineAction(condition)
        val conditionResult = interpreter.interpret(condition, conditionAction)

        return if (conditionResult == true) {
            val thenAction = interpreter.determineAction(thenBlock)
            interpreter.interpret(thenBlock, thenAction) ?: Unit
        } else if (elseBlock != null) {
            val elseAction = interpreter.determineAction(elseBlock)
            interpreter.interpret(elseBlock, elseAction) ?: Unit
        } else {
            Unit
        }
    }
}
