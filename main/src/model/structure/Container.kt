package model.structure

class Container {

    val container = mutableListOf<Token>() // Ahora es privada

    fun addContainer(token: Token){
        container.add(token)
    }

    fun getTokens(): List<Token> = container.toList() // Devuelve copia inmutable
}