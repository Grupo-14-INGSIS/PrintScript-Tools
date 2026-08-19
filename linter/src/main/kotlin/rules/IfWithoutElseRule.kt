package linter.src.main.kotlin.rules

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.LintRule

class IfWithoutElseRule : LintRule {
    override fun apply(root: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()

        fun isControlFlow(node: ASTNode): Boolean {
            val type = node.type
            return type == ASTNodeType.PRINTLN || node.content in listOf("return", "throw", "continue")
        }

        fun traverse(node: ASTNode) {
            if (node.type == ASTNodeType.IF_KEYWORD || node.type == ASTNodeType.IF_STATEMENT) {
                val elseBranch = node.children.getOrNull(2) // condition, thenBlock, optional elseBlock
                val thenBlock = node.children.getOrNull(1)
                val thenLast = thenBlock?.children?.lastOrNull()

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
