package model.tools.interpreter.parser

import model.structure.ASTNode
import model.structure.Container;
import model.structure.DataType
import model.structure.Token

class PrattParser {

    fun arithParse(tokens: Container): ASTNode {
        return ASTNode(Token(DataType.INVALID, "", 0), listOf())
    }

}