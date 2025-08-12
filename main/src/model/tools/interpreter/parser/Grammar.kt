package model.tools.interpreter.parser

import model.structure.ASTNode
import model.structure.Container
import model.structure.DataType
import model.structure.Token

class Grammar {

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.size() == 0) {
            return ASTNode(Token(DataType.INVALID, "", 0), listOf())
        }
        else if (tokens.get(0)!!.type == DataType.LET_KEYWORD) {
            return ASTNode(tokens.get(), listOf())
        }
    }

}