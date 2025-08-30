package linter.rules

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import linter.LintError
import linter.LintRule

class IdentifierNamingRule(private val style: String = "camelCase") : LintRule {

    override fun apply(root: ASTNode): List<LintError> = checkNode(root)

    private fun checkNode(node: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        if (node.token.type == DataType.IDENTIFIER) {
            val name = node.token.content
            val isValid = when (style) {
                "camelCase" -> name.matches(Regex("^[a-z][a-zA-Z0-9]*$"))
                "snake_case" -> name.matches(Regex("^[a-z]+(_[a-z0-9]+)*$"))
                else -> true
            }
            if (!isValid) {
                val pos = node.token.position
                errors += LintError("Identifier '$name' does not match $style style", Position(pos.line, pos.column))
            }
        }

        node.children.forEach { child ->
            errors += checkNode(child)
        }

        return errors
    }
}
