package linter

import common.src.main.kotlin.ASTNode

interface LintRule {
    fun apply(root: ASTNode): List<LintError>
}
