package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

/**
 * Abstracción que provee el contexto de ejecución, acceso a I/O y operaciones de memoria/scope.
 * Permite cumplir Inversión de Dependencias (DIP) y Segregación de Interfaces (ISP).
 */
interface ExecutionContext {
    val version: String
    val printer: (Any?) -> Unit

    fun enterScope()
    fun exitScope()
    fun declareVariable(name: String, value: Any?, type: String)
    fun assignVariable(name: String, value: Any?)
    fun resolveVariable(name: String): Any?
    fun resolveVariableType(name: String): String?
    fun declareConstant(name: String, value: Any?, type: String)
    fun interpret(node: ASTNode): Any?
}
