package linter

import ast.src.main.kotlin.ASTNode

class Linter(private val rules: List<LintRule>) {

    fun all(root: ASTNode): List<LintError> = rules.flatMap { it.apply(root) }

    fun allPassed(root: ASTNode): Boolean = all(root).isEmpty()
}

// recibo una lista de rules, de tipo LintRule. dado que son private val, son inmutables
// corre todas las reglas sobre el mismo ASTNode. Flatmap genera una lista con todos los LintError y los devuelve
// mira si la lista de errores esta vacia o no para decidir si pasa el linter o no
