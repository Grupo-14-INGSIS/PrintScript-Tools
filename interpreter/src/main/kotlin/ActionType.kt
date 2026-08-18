package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

/**
 * Abstracción para el manejo y ejecución de cada acción o nodo AST (SRP y OCP).
 * Depende de la interfaz ExecutionContext en lugar de la clase concreta Interpreter (DIP).
 */
interface ActionType {
    fun interpret(node: ASTNode, interpreter: ExecutionContext): Any
}
