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
    return if (token.content in precedences) {
        precedences[token.content]!!
    } else {
        0
    }
}

fun getAssociativity(token: Token): Association {
    return if (token.content in associations) {
        associations[token.content]!!
    } else {
        Association.ANY
    }
}

class PrattToken(
    private val token: Token,
    private var precedence: Int = getPrecedence(token),
    private var associativity: Association = getAssociativity(token),
    private val children: MutableList<PrattToken> = mutableListOf()
) {

    fun token(): Token {
        return token
    }

    fun associate(children: List<PrattToken>) {
        this.precedence = 0
        this.associativity = Association.ANY
        this.children.addAll(children)
    }

    fun precedence(): Int {
        return precedence
    }

    fun associativity(): Association {
        return associativity
    }

    fun children(index: Int): PrattToken? {
        if (index < 0 || index >= children.size) return null
        return children[index]
    }
}
