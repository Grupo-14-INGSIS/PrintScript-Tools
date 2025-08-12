package Interpreter

import model.structure.ASTNode

interface ActionType {
    fun interpret(node:ASTNode, actions: Actions) : Any;
}