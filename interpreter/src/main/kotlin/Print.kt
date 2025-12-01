package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        val value = interpreter.interpret(node.children[0])
        if (value is Double && value % 1 == 0.0) {
            interpreter.printer(value.toInt())
            return value.toInt().toString()
        } else {
            interpreter.printer(value)
            return value.toString()
        }
    }
}
