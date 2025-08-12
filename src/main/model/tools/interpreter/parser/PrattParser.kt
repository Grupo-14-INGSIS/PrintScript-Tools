package src.main.model.tools.interpreter.parser

import src.main.model.structure.ASTNode
import src.main.model.structure.Container;
import src.main.model.structure.DataType
import src.main.model.structure.Token

class PrattParser {

    fun arithParse(tokens: Container): ASTNode {
        return ASTNode(Token(DataType.INVALID, "", 0), listOf())
    }

}