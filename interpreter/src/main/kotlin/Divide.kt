package src.main.model.tools.interpreter.interpreter

import common.src.main.kotlin.ASTNode

object Divide : ActionType {
    override fun interpret(node: ASTNode, action: Actions) : Any {
        val left = node.children[0]
        val right = node.children[1]

        val leftValue = left.token.content
        val rightValue = right.token.content

        val leftNum = leftValue.toIntOrNull()
        val rightNum = rightValue.toIntOrNull()

        if(rightNum == 0){
            return false
        }
        if (leftNum != null && rightNum != null) {
            return leftNum / rightNum
        }
        return false
    }
}