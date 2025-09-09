package linter.src.main.kotlin

import ast.src.main.kotlin.ASTNode

interface LintRule {
    fun apply(root: ASTNode): List<LintError>
}
