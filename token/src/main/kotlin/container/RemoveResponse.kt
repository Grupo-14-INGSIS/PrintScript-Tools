package container.src.main.kotlin

import token.src.main.kotlin.Token

data class RemoveResponse(
    val token: Token?,
    val container: Container
)
