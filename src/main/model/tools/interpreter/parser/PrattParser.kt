package src.main.tools.interpreter.parser

import src.main.structure.ASTNode
import src.main.structure.Container;
import src.main.structure.DataType
import src.main.structure.Token

class PrattParser {

    fun arithParse(tokens: Container): ASTNode {
        return ASTNode(Token(DataType.INVALID, "", 0), listOf())
    }

}