package parser.src.main.kotlin

import token.src.main.kotlin.Token

class PrattToken(
    val token: Token,
    val precedence: Int,
    val associativity: Association,
    val children: List<PrattToken> = emptyList()
) {

    fun token(): Token = token

    fun associate(newChildren: List<PrattToken>): PrattToken {
        return PrattToken(
            token = this.token,
            precedence = 0, // Se resetea al asociar
            associativity = Association.ANY,
            children = newChildren.toList()
        )
    }

    fun precedence(): Int = precedence

    fun associativity(): Association = associativity

    fun children(index: Int): PrattToken? {
        return if (index in children.indices) children[index] else null
    }

    fun allChildren(): List<PrattToken> = children.toList()
}

