package common.src.main.kotlin

class Container {

    val container = mutableListOf<Token>() // la puedo hacer immutable? -> Si le haces .add(), creo que no puede ser inmutable

    fun addContainer(token: Token) {
        container.add(token)
    }

    fun addAt(token: Token, index: Int): Boolean {
        if (0 > index || index > container.size) return false
        container.add(index, token)
        return true
    }

    fun get(index: Int): Token? {
        return if (index in 0..<size()) {
            container[index]
        } else {
            null
        }
    }

    fun remove(index: Int): Token? {
        return if (index in 0..<size()) {
            container.removeAt(index)
        } else {
            null
        }
    }

    fun first(): Token? {
        return if (container.isNotEmpty()) container.first() else null
    }

    fun last(): Token? {
        return if (container.isNotEmpty()) container.last() else null
    }

    fun sliceOne(at: Int): Container {
        if (at < 0 || at >= size()) return Container()
        val output = Container()
        output.addContainer(get(at)!!)
        return output
    }

    fun slice(from: Int, to: Int = size()): Container {
        if (from > to) return Container()

        val safeFrom = if (from < 0) 0 else from
        val safeTo = if (to >= size()) size() else to

        val output = Container()
        for (i in safeFrom until safeTo) {
            output.addContainer(get(i)!!)
        }
        return output
    }

    fun size(): Int {
        return container.size
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }

    fun checkIs(tokens: List<DataType>): Boolean {
        if (size() != tokens.size) return false
        for (i in 0..size()) {
            if (get(i)!!.type == tokens[i]) return false
        }
        return true
    }
}
