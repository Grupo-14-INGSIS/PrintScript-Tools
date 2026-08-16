package linter.src.main.kotlin.rules

import ast.src.main.kotlin.ASTNode
import linter.src.main.kotlin.LintError

interface LintRule {
    fun apply(root: ASTNode): List<LintError>
}
