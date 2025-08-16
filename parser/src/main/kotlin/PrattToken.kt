package parser.src.main.kotlin

import common.src.main.kotlin.Token

private val precedences: Map<String, Int> = mapOf(
    "*" to 2,
    "/" to 2,
    "+" to 1,
    "-" to 1,
    "=" to 0
)

private val associations: Map<String, Association> = mapOf(
    "*" to Association.LEFT,
    "/" to Association.LEFT,
    "+" to Association.LEFT,
    "-" to Association.LEFT,
    "=" to Association.RIGHT
)

fun getPrecedence(token: Token): Int {
    return precedences[token.content]!!
}

fun getAssociativity(token: Token): Association {
    return associations[token.content]!!
}

class PrattToken (
    private val token: Token,
    private val precedence: Int = getPrecedence(token),
    private val associativity: Association = getAssociativity(token),
    private val children: MutableList<Token> = mutableListOf()
) {

    fun token(): Token {
        return token
    }

    fun associate(child1: PrattToken, child2: PrattToken?): PrattToken {
        val output: PrattToken = PrattToken(token, 0)
        output.children.add(child1.token())
        if (child2 != null) output.children.add(child2.token())
        return output
    }

    fun precedence(): Int {
        return precedence
    }

    fun associativity(): Association {
        return associativity
    }

}