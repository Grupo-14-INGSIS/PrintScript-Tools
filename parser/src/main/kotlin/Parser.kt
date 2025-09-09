package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType

class Parser(
    private var tokens: Container
) {

    private val grammar = Grammar()

    fun parse(): ASTNode {
        val line: Container = format()
        return grammar.stmtParse(line)
    }

    private fun format(): Container {
        var output = Container()
        val space = DataType.SPACE
        val semicolon = DataType.SEMICOLON
        for (i in 0 until tokens.size()) {
            if (tokens.get(i)!!.type != space && tokens.get(i)!!.type != semicolon) {
                output = output.addContainer(tokens.get(i)!!)
            }
        }
        return output
    }
}
