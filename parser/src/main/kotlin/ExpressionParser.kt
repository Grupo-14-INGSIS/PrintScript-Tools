package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container

/**
 * Abstracción que define las operaciones de parsing de expresiones y bloques.
 * Permite cumplir el principio de Inversión de Dependencias (DIP) y Segregación de Interfaces (ISP),
 * evitando que los parsers de sentencias dependan de la clase concreta Parser.
 */
interface ExpressionParser {
    fun expParse(tokens: Container): ASTNode
    fun ifStmtParse(tokens: Container): ASTNode
    fun stmtParse(tokens: Container): ASTNode
    fun parseBlock(tokens: Container): ASTNode
}
