package src.main.model.tools.interpreter.interpreter

import ast.src.main.kotlin.ASTNode

object Divide : ActionType {
    override fun interpret(node: ASTNode): Any {
        val left = node.children[0]
        val right = node.children[1]

        val leftValue = left.content
        val rightValue = right.content

        val leftNum = leftValue.toDoubleOrNull()
        val rightNum = rightValue.toDoubleOrNull()

        if (rightNum == 0.0) {
            return false
        }
        if (leftNum != null && rightNum != null) {
            return leftNum / rightNum
        }
        return false
    }
}
