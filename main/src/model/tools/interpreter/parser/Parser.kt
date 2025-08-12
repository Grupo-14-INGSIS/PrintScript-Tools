package model.tools.interpreter.parser

import model.structure.ASTNode
import model.structure.Container
import model.tools.interpreter.common.Command

class Parser {

    val grammar = Grammar()

    fun parse(tokens: Container): ASTNode {
        return grammar.stmtParse(tokens)
    }

    fun ignoreSpaces(tokens: Container): Container {
        var output: List<Token> = mutableListOf()
    }

}