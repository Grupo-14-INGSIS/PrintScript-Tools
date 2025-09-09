package container.src.main.kotlin

import token.src.main.kotlin.Token

// Inmutable class
class Container(
    val container: List<Token> = listOf()
) {

    fun addContainer(token: Token): Container {
        val newContainer = mutableListOf<Token>()
        newContainer.addAll(container)
        newContainer.add(token)
        return Container(newContainer)
    }

    fun addAll(contents: List<Token>): Container {
        val newContainer = mutableListOf<Token>()
        newContainer.addAll(container)
        newContainer.addAll(contents)
        return Container(newContainer)
    }

    fun addAt(token: Token, index: Int): Container {
        val realIndex = if (index < 0) 0 else if (size() < index) size() else index
        val newContainer = mutableListOf<Token>()
        newContainer.addAll(container)
        newContainer.add(realIndex, token)
        return Container(newContainer)
    }

    fun get(index: Int): Token? {
        return if (index in 0 until size()) {
            container[index]
        } else {
            null
        }
    }

    fun remove(index: Int): RemoveResponse {
        if (index in 0 until size()) {
            val newContainer = mutableListOf<Token>()
            newContainer.addAll(container)
            val deletedToken = newContainer.removeAt(index)
            return RemoveResponse(deletedToken, Container(newContainer))
        } else {
            return RemoveResponse(null, this)
        }
    }

    fun first(): Token? {
        return if (container.isNotEmpty()) container.first() else null
    }

    fun last(): Token? {
        return if (container.isNotEmpty()) container.last() else null
    }

    fun take(at: Int): Container {
        if (at < 0 || at >= size()) return Container()
        var output = Container()
        output = output.addContainer(get(at)!!)
        return output
    }

    fun slice(from: Int, to: Int = size()): Container {
        if (from >= to) return Container()

        val safeFrom = if (from < 0) 0 else from
        val safeTo = if (to > size()) size() else to

        val output: List<Token> = container.subList(safeFrom, safeTo)
        return Container(output)
    }

    fun size(): Int {
        return container.size
    }

    fun isEmpty(): Boolean {
        return size() == 0
    }
}
