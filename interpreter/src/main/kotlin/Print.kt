package interpreter.src.main.kotlin
import ast.src.main.kotlin.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode) {
        val value = node.children[0].content // children[0] es el token
        println(value)
        // no vale mucho la pena un return Unit
    }
}
