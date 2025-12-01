package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Multiply : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val left = interpreter.interpret(node.children[0])
        val right = interpreter.interpret(node.children[1])

        return when {
            left is Double && right is Double -> left * right
            left is Int && right is Double -> left.toDouble() * right
            left is Double && right is Int -> left * right.toDouble()
            left is Int && right is Int -> left * right
            else -> throw IllegalArgumentException(
                "Cannot multiply types ${left?.javaClass?.simpleName} and ${right?.javaClass?.simpleName}"
            )
        }
    }
}
