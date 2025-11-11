package linter.src.main.kotlin.rules

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.LintRule

class ReadInputRule(private val enabled: Boolean = true) : LintRule {

    override fun apply(root: ASTNode): List<LintError> =
        if (!enabled) emptyList() else checkNode(root)

    private fun checkNode(node: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        if (node.type == DataType.READ_INPUT) {
            val arg = node.children.firstOrNull()
            if (arg != null && arg.type !in listOf(DataType.IDENTIFIER, DataType.NUMBER_LITERAL, DataType.STRING_LITERAL)) {
                val pos = node.position
                errors += LintError(
                    "readInput argument must be a literal or identifier",
                    Position(
                        pos.line,
                        pos.column
                    )
                )
            }
        }

        node.children.forEach { child ->
            errors += checkNode(child)
        }

        return errors
    }
}
