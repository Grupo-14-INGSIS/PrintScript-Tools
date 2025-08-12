package src.main.tools.interpreter.interpreter

import src.main.structure.ASTNode

object Print : ActionType {
    override fun interpret(node: ASTNode, actions: Actions) {
        val value = node.children[0].token.content //children[0] es el token
        println(value)
        //no vale mucho la pena un return Unit
    }
}