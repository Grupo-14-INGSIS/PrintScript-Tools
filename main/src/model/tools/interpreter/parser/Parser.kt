package model.tools.interpreter.parser

import model.structure.ASTNode
import model.structure.Container
import model.structure.Token
import model.structure.DataType

class Parser (
    private var tokens: Container
) {

    private val grammar = Grammar()

    fun parse(): ASTNode {
        val line: Container = format()
        return grammar.stmtParse(line)
    }

    private fun format(): Container {
        val output: Container = Container()
        val space = DataType.SPACE
        val semicolon = DataType.SEMICOLON
        for (i in 0 until tokens.size()) {
            if (tokens.get(i)!!.type != space && tokens.get(i)!!.type != semicolon) {
                output.addContainer(tokens.get(i)!!)
            }
        }
        return output
    }
}