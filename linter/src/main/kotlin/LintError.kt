package linter.src.main.kotlin

import ast.src.main.kotlin.Position

data class LintError(
    val message: String,
    val position: Position
) {
    override fun toString(): String = "[Linter] Row $position.line, Column $position.column: $message"
}
