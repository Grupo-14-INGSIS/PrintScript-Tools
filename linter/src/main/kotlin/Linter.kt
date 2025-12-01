package linter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

class Linter(private val rules: List<LintRule>) {

    // New main method for multi-AST analysis
    fun lint(asts: List<ASTNode>): List<LintError> {
        return asts.flatMap { ast ->
            rules.flatMap { rule ->
                rule.apply(ast)
            }
        }
    }

    fun all(root: ASTNode): List<LintError> = rules.flatMap { it.apply(root) }

    fun lintingPassed(asts: List<ASTNode>): Boolean = lint(asts).isEmpty()
}

// recibo una lista de rules, de tipo LintRule. dado que son private val, son inmutables
// corre todas las reglas sobre el mismo ASTNode. Flatmap genera una lista con todos los LintError y los devuelve
// mira si la lista de errores esta vacia o no para decidir si pasa el linter o no
