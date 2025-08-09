package model.tools.interpreter.common

object VariableStorage {
    private val variables: MutableMap<String, Any> = mutableMapOf()

    /**
     * Allows to store a variable `name` associated to `value`.
     */
    fun store(name: String, value: Any) {
        variables[name] = value
    }

    /**
     * Allows to fetch a variable `name` and returns its associated `value`, or `Null` if it was not stored before.
     */
    fun get(name: String): Any? {
        return variables[name]
    }

    /**
     * Allows to delete the variable `name`, returning it's previously associated `value`, or `Null` if it was not stored before.
     */
    fun delete(name: String): Any? {
        return variables.remove(name)
    }
}