package lexer.src.main.kotlin

class PeekingIterator<T>(private val iterator: Iterator<T>) : Iterator<T> {
    private var nextElement: T? = null
    private var hasNextElement = false

    init {
        primeNext()
    }

    private fun primeNext() {
        if (iterator.hasNext()) {
            nextElement = iterator.next()
            hasNextElement = true
        } else {
            nextElement = null
            hasNextElement = false
        }
    }

    override fun hasNext(): Boolean = hasNextElement

    override fun next(): T {
        if (!hasNextElement) throw NoSuchElementException()
        val result = nextElement!!
        primeNext()
        return result
    }

    fun peek(): T {
        if (!hasNextElement) throw NoSuchElementException()
        return nextElement!!
    }
}
