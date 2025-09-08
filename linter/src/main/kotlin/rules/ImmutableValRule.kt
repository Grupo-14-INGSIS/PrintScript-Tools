package linter.rules

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import linter.LintError
import linter.LintRule

class ImmutableValRule : LintRule {
    override fun apply(root: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()
        val declaredVars = mutableMapOf<String, ASTNode>()
        val reassigned = mutableSetOf<String>()

        fun traverse(node: ASTNode) {
            val token = node.token

            if (token.type == DataType.DECLARATION && token.content == "var") {
                val identifierNode = node.children.firstOrNull { it.token.type == DataType.IDENTIFIER }
                val name = identifierNode?.token?.content
                if (name != null) {
                    declaredVars[name] = node
                }
            }

            if (token.type == DataType.ASSIGNATION) {
                val targetNode = node.children.firstOrNull { it.token.type == DataType.IDENTIFIER }
                val name = targetNode?.token?.content
                if (name != null) {
                    reassigned.add(name)
                }
            }

            node.children.forEach { traverse(it) }
        }

        traverse(root)

        for ((name, node) in declaredVars) {
            if (!reassigned.contains(name)) {
                errors.add(
                    LintError(
                        message = "Variable '$name' declared with 'var' but never reassigned. Use 'val' instead.",
                        position = node.token.position
                    )
                )
            }
        }

        return errors
    }
}
