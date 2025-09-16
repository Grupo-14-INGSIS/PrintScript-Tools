package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class PrattParser(private val features: VersionFeatures) {

    private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())
    private val tokenFactory = PrattTokenFactory(features)

    fun arithParse(tokens: Container): ASTNode {
        val symbols: List<PrattToken> = prattify(tokens)
        val result = processTokens(symbols)
        return if (result.size == 1) prattToAST(result[0]) else invalid
    }

    private fun processTokens(symbols: List<PrattToken>): List<PrattToken> {
        if (symbols.size <= 1) return symbols

        val nextOperator = highestPrecedIndex(symbols)
        if (nextOperator == -1) return symbols

        val newSymbols = associateOperation(symbols, nextOperator)

        /*
        if (newSymbols.size >= symbols.size) {
            println("ERROR: La lista no se redujo! Posible bucle infinito")
            println("Original: ${symbols.map { it.token().content }}")
            println("Nueva: ${newSymbols.map { it.token().content }}")
            return symbols // Evitar recursión infinita
        }

         */

        return processTokens(newSymbols) // ✅ Recursión inmutable
    }

    private fun associateOperation(symbols: List<PrattToken>, operator: Int): List<PrattToken> {
        if (operator - 1 < 0 || operator + 1 >= symbols.size) return symbols

        val left = symbols[operator - 1]
        val right = symbols[operator + 1]
        val operatorToken = symbols[operator]

        val associatedToken = operatorToken.associate(listOf(left, right))

        val newList = mutableListOf<PrattToken>()
        newList.addAll(symbols)
        newList[operator] = associatedToken

        newList.removeAt(operator + 1)
        newList.removeAt(operator - 1)

        /*
        newList.addAll(symbols.subList(0, operator - 1)) // Antes del operador
        newList.add(associatedToken) // Token asociado
        newList.addAll(symbols.subList(operator + 2, symbols.size)) // Después
         */

        return newList
    }


    private fun prattify(tokens: Container): List<PrattToken> {
        return (0 until tokens.size()).map { i ->
            tokenFactory.createPrattToken(tokens.get(i)!!)
        }
    }

    private fun highestPrecedIndex(symbols: List<PrattToken>): Int {
        var output = -1
        var highestPrecedence = -1

        for (i in symbols.indices) {
            val token = symbols[i]
            when {
                token.precedence() > highestPrecedence -> {
                    highestPrecedence = token.precedence()
                    output = i
                }
                token.precedence() == highestPrecedence &&
                    token.associativity() == Association.RIGHT -> {
                    output = i
                }
            }
        }
        return output
    }

    private fun prattToAST(symbol: PrattToken): ASTNode {
        val children = symbol.allChildren().map { prattToAST(it) }

        return ASTNode(
            symbol.token().type,
            symbol.token().content,
            symbol.token().position,
            children
        )
    }
}
