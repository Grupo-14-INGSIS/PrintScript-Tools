package src.main.tools.interpreter.interpreter

import src.main.structure.ASTNode

object Multiply : ActionType {
    override fun interpret(node: ASTNode, action: Actions) : Any {
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