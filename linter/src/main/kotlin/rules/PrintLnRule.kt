package linter.rules

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import linter.LintError
import linter.LintRule

class PrintLnRule(private val enabled: Boolean = true) : LintRule {

    override fun apply(root: ASTNode): List<LintError> =
        if (!enabled) emptyList() else checkNode(root)

    private fun checkNode(node: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        if (node.token.type == DataType.PRINTLN) {
            val arg = node.children.firstOrNull()
            if (arg != null && arg.token.type !in listOf(DataType.IDENTIFIER, DataType.NUMBER_LITERAL, DataType.STRING_LITERAL)) {
                val pos = node.token.position
                errors += LintError(
                    "println argument must be a literal or identifier",
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
