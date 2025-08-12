package src.main.model.tools.interpreter.interpreter

import src.main.model.structure.ASTNode

interface ActionType {
    fun interpret(node:ASTNode, actions: Actions) : Any;
}