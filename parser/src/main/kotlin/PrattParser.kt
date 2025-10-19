package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class PrattParser(private val features: VersionFeatures) {

    private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())
    private val tokenFactory = PrattTokenFactory(features)
    private var recursionDepth = 0
    private val MAX_RECURSION_DEPTH = 1000

    fun arithParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) return invalid

        recursionDepth = 0
        val symbols: List<PrattToken> = prattify(tokens)
        val result = processTokens(symbols)

        return if (result.size == 1) {
            prattToAST(result[0])
        } else {
            invalid
        }
    }

    private fun processTokens(symbols: List<PrattToken>): List<PrattToken> {
        recursionDepth++
        if (recursionDepth > MAX_RECURSION_DEPTH) {
            println("ERROR: Max recursion depth reached in processTokens")
            return symbols
        }

        // Base case: single token or no operators left
        if (symbols.size <= 1) return symbols

        val nextOperator = highestPrecedIndex(symbols)

        // No valid operator found
        if (nextOperator == -1) return symbols

        // Validate operator has left and right operands
        if (nextOperator - 1 < 0 || nextOperator + 1 >= symbols.size) {
            println("ERROR: Operator at invalid position: $nextOperator in list of size ${symbols.size}")
            return symbols
        }

        val newSymbols = associateOperation(symbols, nextOperator)

        // Safety check: ensure list is actually getting smaller
        if (newSymbols.size >= symbols.size) {
            println("ERROR: List not reducing in processTokens")
            println("Original size: ${symbols.size}, New size: ${newSymbols.size}")
            return symbols
        }

        return processTokens(newSymbols)
    }

    private fun associateOperation(symbols: List<PrattToken>, operatorIndex: Int): List<PrattToken> {
        val left = symbols[operatorIndex - 1]
        val right = symbols[operatorIndex + 1]
        val operatorToken = symbols[operatorIndex]

        val associatedToken = operatorToken.associate(listOf(left, right))

        // Build new list without the consumed tokens
        val result = mutableListOf<PrattToken>()

        // Add tokens before the operation
        for (i in 0 until operatorIndex - 1) {
            result.add(symbols[i])
        }

        // Add the associated token
        result.add(associatedToken)

        // Add tokens after the operation
        for (i in operatorIndex + 2 until symbols.size) {
            result.add(symbols[i])
        }

        return result
    }

    private fun prattify(tokens: Container): List<PrattToken> {
        val result = mutableListOf<PrattToken>()
        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)
            if (token != null) {
                result.add(tokenFactory.createPrattToken(token))
            }
        }
        return result
    }

    private fun highestPrecedIndex(symbols: List<PrattToken>): Int {
        var outputIndex = -1
        var highestPrecedence = -1

        for (i in symbols.indices) {
            val token = symbols[i]
            val currentPrecedence = token.precedence()

            // Skip tokens with no precedence (operands)
            if (currentPrecedence <= 0) continue

            when {
                // Higher precedence found
                currentPrecedence > highestPrecedence -> {
                    highestPrecedence = currentPrecedence
                    outputIndex = i
                }
                // Same precedence, check associativity
                currentPrecedence == highestPrecedence &&
                    token.associativity() == Association.RIGHT -> {
                    outputIndex = i
                }
            }
        }

        return outputIndex
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
