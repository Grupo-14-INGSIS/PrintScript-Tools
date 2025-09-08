package src.main.model.tools.interpreter.interpreter

import common.src.main.kotlin.ASTNode

object Multiply : ActionType {
    override fun interpret(node: ASTNode): Any {
        val left = node.children[0]
        val right = node.children[1]

        val leftValue = left.token.content
        val rightValue = right.token.content

        val leftNum = leftValue.toDoubleOrNull()
        val rightNum = rightValue.toDoubleOrNull()

        if (leftNum != null && rightNum != null) {
            return leftNum * rightNum
        }

        return false
    }
}
