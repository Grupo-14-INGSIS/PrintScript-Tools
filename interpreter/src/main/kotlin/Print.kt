package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val value = interpreter.interpret(node.children[0])
        if (value is Double && value % 1 == 0.0) {
            println(value.toInt())
        } else {
            println(value)
        }
        return Unit
    }
}
