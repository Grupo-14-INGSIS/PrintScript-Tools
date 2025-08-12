package Interpreter

import model.structure.ASTNode

object Subtract : ActionType{
    override fun interpret(node: ASTNode, action: Actions) : Any {
        val left = node.children[0]
        val right = node.children[1]

        val leftValue = left.content
        val rightValue = right.content

        val leftNum = leftValue.toDoubleOrNull()
        val rightNum = rightValue.toDoubleOrNull()

        if (leftNum != null && rightNum != null) {
            return leftNum - rightNum
        }

        // no puede restar texto
        return false
    }
}