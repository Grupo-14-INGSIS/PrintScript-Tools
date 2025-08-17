package parser.src.main.kotlin

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token

class PrattParser {

    private val invalid = ASTNode(Token(DataType.INVALID, "", 0), listOf())

    fun arithParse(tokens: Container): ASTNode {
        val symbols: MutableList<PrattToken> = prattify(tokens)
        var nextOperator: Int
        var result: Boolean
        while (symbols.size > 1) {
            nextOperator = highestPrecedIndex(symbols)
            if (nextOperator == -1) return invalid
            result = associate(symbols, nextOperator)
            if (!result) return invalid
        }
        return prattToAST(symbols[0])
    }

    // Turns a Container into a list of PrattToken objects
    private fun prattify(tokens: Container): MutableList<PrattToken> {
        val output: MutableList<PrattToken> = mutableListOf()
        for (i in 0 until tokens.size()) {
            output.add(PrattToken(tokens.get(i)!!))
        }
        return output
    }

    // Searches for symbol with the highest precedence
    private fun highestPrecedIndex(symbols: List<PrattToken>): Int {
        var output: Int = -1 // Index of highest precedence
        var highestPrecedence: Int = symbols[0].precedence() // Highest precedence value
        var token: PrattToken
        for (i in symbols.indices) {
            token = symbols[i]
            if (token.precedence() > highestPrecedence) {
                highestPrecedence = token.precedence()
                output = i
            } else if (token.precedence() == highestPrecedence) {
                if (token.associativity() == Association.RIGHT) {
                    output = i
                } // else keep left association
            }
        }
        return output
    }

    // Solves association for a specific operator
    private fun associate(symbols: MutableList<PrattToken>, operator: Int): Boolean {
        if (operator - 1 < 0 || operator + 1 >= symbols.size) return false
        val left = symbols[operator - 1]
        val right = symbols[operator + 1]
        symbols[operator].associate(listOf(left, right))
        symbols.removeAt(operator + 1)
        symbols.removeAt(operator - 1)
        return true
    }

    // Takes a pratt token tree and turns it into an AST node tree
    private fun prattToAST(symbol: PrattToken): ASTNode {
        val children: MutableList<ASTNode> = mutableListOf()
        if (symbol.children(0) != null) {
            children.add(prattToAST(symbol.children(0)!!))
        }
        if (symbol.children(1) != null) {
            children.add(prattToAST(symbol.children(1)!!))
        }
        val output = ASTNode(symbol.token(), children)
        return output
    }
}