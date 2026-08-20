package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType

object VarDeclarationOnly : ActionType {
    override fun interpret(node: ASTNode, interpreter: ExecutionContext): Any {
        require(node.children.size == 1) { "Invalid variable declaration structure" }

        val declarationInfoNode = node.children[0]
        require(declarationInfoNode.children.size == 2) { "Invalid declaration info structure" }

        val keywordType = declarationInfoNode.type
        val variableName = declarationInfoNode.children[0].content
        val variableType = declarationInfoNode.children[1].content

        val defaultValue: Any? = when (variableType.lowercase()) {
            "number" -> 0
            "string" -> ""
            "boolean" -> false
            else -> null
        }

        if (keywordType == ASTNodeType.CONST_KEYWORD) {
            interpreter.declareConstant(variableName, defaultValue, variableType)
        } else {
            interpreter.declareVariable(variableName, defaultValue, variableType)
        }

        return Unit
    }
}
