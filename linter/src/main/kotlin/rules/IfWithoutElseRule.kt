package linter.rules

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import linter.LintError
import linter.LintRule

class IfWithoutElseRule : LintRule {
    override fun apply(root: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        fun isControlFlow(node: ASTNode): Boolean {
            val type = node.token.type
            return type == DataType.PRINTLN || node.token.content in listOf("return", "throw", "continue")
        }

        fun traverse(node: ASTNode) {
            if (node.token.type == DataType.IF_KEYWORD) {
                val thenBranch = node.children.firstOrNull { it.token.type == DataType.OPEN_BRACE }
                val elseBranch = node.children.firstOrNull { it.token.type == DataType.ELSE_KEYWORD }

                val thenLast = thenBranch?.children?.lastOrNull()

                if (elseBranch == null && (thenLast == null || !isControlFlow(thenLast))) {
                    errors.add(
                        LintError(
                            message = "If block at ${node.token.position} lacks 'else' and does not end with control flow.",
                            position = node.token.position
                        )
                    )
                }
            }

            node.children.forEach { traverse(it) }
        }

        traverse(root)
        return errors
    }
}
