package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.DataType

object VarDeclarationOnly : ActionType {
    override fun interpret(node: ASTNode, interpreter: Interpreter): Any {
        // The VAR_DECLARATION_WITHOUT_ASSIGNATION node has one child:
        // ASTNode(keyword, identifier.content, ..., children = [AST(IDENTIFIER), AST(TYPE)])
        require(node.children.size == 1) { "Invalid variable declaration structure" }

        val declarationInfoNode = node.children[0]
        require(declarationInfoNode.children.size == 2) { "Invalid declaration info structure" }

        val keywordType = declarationInfoNode.type // LET_KEYWORD or CONST_KEYWORD
        val variableName = declarationInfoNode.children[0].content
        val variableType = declarationInfoNode.children[1].content // e.g., "number", "string"

        val defaultValue: Any? = when (variableType.lowercase()) {
            "number" -> 0
            "string" -> ""
            "boolean" -> false
            else -> null // For unsupported types or if default is truly null
        }

        if (keywordType == DataType.CONST_KEYWORD) {
            interpreter.declareConstant(variableName, defaultValue, variableType)
        } else {
            interpreter.declareVariable(variableName, defaultValue, variableType)
        }

        return Unit
    }
}
