package src.main.model.tools.interpreter.interpreter

import common.src.main.kotlin.ASTNode

interface ActionType {
    fun interpret(node: ASTNode, actions: Actions) : Any;
}