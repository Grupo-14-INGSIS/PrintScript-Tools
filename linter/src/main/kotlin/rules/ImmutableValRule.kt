package linter.src.main.kotlin.rules

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.LintRule

class ImmutableValRule : LintRule {
    override fun apply(root: ASTNode): List<LintError> {
        val errors = mutableListOf<LintError>()
        val declaredVars = mutableMapOf<String, ASTNode>()
        val reassigned = mutableSetOf<String>()

        fun traverse(node: ASTNode) {
            if (node.type == DataType.DECLARATION && node.content == "var") {
                val identifierNode = node.children.firstOrNull { it.type == DataType.IDENTIFIER }
                val name = identifierNode?.content
                if (name != null) {
                    declaredVars[name] = node
                }
            }

            if (node.type == DataType.ASSIGNATION) {
                val targetNode = node.children.firstOrNull { it.type == DataType.IDENTIFIER }
                val name = targetNode?.content
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
                        position = node.position
                    )
                )
            }
        }

        return errors
    }
}
