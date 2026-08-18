package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
        val value = interpreter.interpret(node.children[0])
        val formattedValue = value.formatNumber()
        interpreter.printer(formattedValue)
        return formattedValue
    }
}
