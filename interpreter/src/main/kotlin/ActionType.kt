package src.main.model.tools.interpreter.interpreter

import common.src.main.kotlin.ASTNode

interface ActionType {
    fun interpret(node: ASTNode): Any
}
// la verdad es que no se usa actions, ya la clase define la accion!
