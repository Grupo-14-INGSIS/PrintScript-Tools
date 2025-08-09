package model.structure

class Container {

    val container = mutableListOf<Token>() //la puedo hacer inmutbale?

    fun addContainer(token: Token){
        container.add(token)
    }
}