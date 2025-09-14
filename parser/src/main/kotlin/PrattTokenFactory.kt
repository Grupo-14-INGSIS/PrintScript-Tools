package parser.src.main.kotlin

import token.src.main.kotlin.Token

class PrattTokenFactory(private val features: VersionFeatures) {

    fun createPrattToken(token: Token): PrattToken {
        val precedence = features.operators[token.content] ?: 0
        val associativity = features.associations[token.content] ?: Association.ANY

        return PrattToken(token, precedence, associativity)
    }
}
