package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val value = interpreter.interpret(node.children[0])
        return if (value is Double && value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
}

