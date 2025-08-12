package src.main.tools.interpreter.interpreter

import src.main.structure.ASTNode

interface ActionType {
    fun interpret(node:ASTNode, actions: Actions) : Any;
}