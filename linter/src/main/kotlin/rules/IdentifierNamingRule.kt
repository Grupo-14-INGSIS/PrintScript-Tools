package linter.src.main.kotlin.rules

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.LintRule
import kotlin.jvm.JvmOverloads

class IdentifierNamingRule @JvmOverloads constructor(private val style: String = "camelCase") : LintRule {

    override fun apply(root: ASTNode): List<LintError> = checkNode(root)

    private fun checkNode(node: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        if (node.type == DataType.IDENTIFIER) {
            val name = node.content
            val isValid = when (style) {
                "camelCase" -> name.matches(Regex("^[a-z][a-zA-Z0-9]*$"))
                "snake_case" -> name.matches(Regex("^[a-z]+(_[a-z0-9]+)*$"))
                else -> true
            }
            if (!isValid) {
                val pos = node.position
                errors += LintError("Identifier '$name' does not match $style style", Position(pos.line, pos.column))
            }
        }

        node.children.forEach { child ->
            errors += checkNode(child)
        }

        return errors
    }
}
