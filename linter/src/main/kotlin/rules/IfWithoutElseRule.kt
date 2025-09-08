package linter.rules

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import linter.LintError
import linter.LintRule

class IfWithoutElseRule : LintRule {
    override fun apply(root: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        fun isControlFlow(node: ASTNode): Boolean {
            val type = node.type
            return type == DataType.PRINTLN || node.content in listOf("return", "throw", "continue")
        }

        fun traverse(node: ASTNode) {
            if (node.type == DataType.IF_KEYWORD) {
                val thenBranch = node.children.firstOrNull { it.type == DataType.OPEN_BRACE }
                val elseBranch = node.children.firstOrNull { it.type == DataType.ELSE_KEYWORD }

                val thenLast = thenBranch?.children?.lastOrNull()

                if (elseBranch == null && (thenLast == null || !isControlFlow(thenLast))) {
                    errors.add(
                        LintError(
                            message = "If block at ${node.position} lacks 'else' and does not end with control flow.",
                            position = node.position
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
