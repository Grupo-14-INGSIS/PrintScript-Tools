package Interpreter

import model.structure.ASTNode

object Divide : ActionType{
    override fun interpret(node: ASTNode, action: Actions) : Any {
        val left = node.children[0]
        val right = node.children[1]

        val leftValue = left.content
        val rightValue = right.content

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