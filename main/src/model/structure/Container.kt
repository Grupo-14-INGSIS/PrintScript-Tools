package model.structure

class Container {

    private val container = mutableListOf<Token>() //la puedo hacer immutable? -> Si le haces .add(), creo que no puede ser inmutable
    private var size: Int = 0;

    fun addContainer(token: Token){
        container.add(token)
        size++
    }

    fun get(index: Int): Token? {
        return if (index < size) container[index] else null
    }

    fun size(): Int {
        return size
    }
}