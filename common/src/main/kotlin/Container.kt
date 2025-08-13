package src.main.model.structure

class Container {

    val container = mutableListOf<Token>() //la puedo hacer immutable? -> Si le haces .add(), creo que no puede ser inmutable
    private var size: Int = 0;

    fun addContainer(token: Token){
        container.add(token)
        size++
    }

    fun get(index: Int): Token? {
        return if (index < size) container[index] else null
    }

    fun first(): Token? {
        return if (container.isNotEmpty()) container.first() else null
    }

    fun last(): Token? {
        return if (container.isNotEmpty()) container.last() else null
    }

    fun sliceOne(at: Int): Container {
        if (at < 0 || at >= size) return Container()
        val output = Container()
        output.addContainer(get(at)!!)
        return output
    }

    fun slice(from: Int, to: Int = size()): Container {
        if (from > to) return Container()

        val safeFrom = if (from < 0) 0 else from
        val safeTo = if (to >= size) size else to

        val output = Container()
        for (i in safeFrom until safeTo) {
            output.addContainer(get(i)!!)
        }
        return output
    }

    fun size(): Int {
        return size
    }

    fun isEmpty(): Boolean {
        return size == 0
    }

    fun checkIs(tokens: List<DataType>): Boolean {
        if (size != tokens.size) return false
        for (i in 0 .. size) {
            if (get(i)!!.type == tokens[i]) return false
        }
        return true
    }
}