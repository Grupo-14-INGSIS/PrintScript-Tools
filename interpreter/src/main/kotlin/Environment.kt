package interpreter.src.main.kotlin

/**
 * Gestor de memoria y ámbitos (Scopes / SymbolTable) del Intérprete.
 * Encapsula la responsabilidad de almacenamiento y resolución de variables, tipos y constantes (SRP).
 */
class Environment {
    private val symbolTable = mutableListOf<MutableMap<String, Any?>>(mutableMapOf())
    private val constants = mutableMapOf<String, Any?>()
    private val variableTypes = mutableListOf<MutableMap<String, String>>(mutableMapOf())

    fun enterScope() {
        symbolTable.add(mutableMapOf())
        variableTypes.add(mutableMapOf())
    }

    fun exitScope() {
        if (symbolTable.size > 1) {
            symbolTable.removeAt(symbolTable.lastIndex)
            variableTypes.removeAt(variableTypes.lastIndex)
        }
    }

    fun declareVariable(name: String, value: Any?, type: String) {
        if (symbolTable.last().containsKey(name)) {
            throw IllegalStateException("Variable '$name' already declared in this scope")
        }
        symbolTable.last()[name] = value
        variableTypes.last()[name] = type
    }

    fun assignVariable(name: String, value: Any?) {
        if (constants.containsKey(name)) {
            throw IllegalStateException("Cannot reassign a constant: '$name'")
        }
        for (scope in symbolTable.asReversed()) {
            if (scope.containsKey(name)) {
                scope[name] = value
                return
            }
        }
        throw IllegalStateException("Variable '$name' not declared")
    }

    fun resolveVariable(name: String): Any? {
        for (scope in symbolTable.asReversed()) {
            if (scope.containsKey(name)) {
                return scope[name]
            }
        }
        if (constants.containsKey(name)) {
            return constants[name]
        }
        throw IllegalStateException("Variable '$name' not declared")
    }

    fun resolveVariableType(name: String): String? {
        for (scope in variableTypes.asReversed()) {
            if (scope.containsKey(name)) {
                return scope[name]
            }
        }
        return null
    }

    fun declareConstant(name: String, value: Any?, type: String) {
        if (constants.containsKey(name)) {
            throw IllegalStateException("Constant '$name' already declared")
        }
        constants[name] = value
        variableTypes.last()[name] = type
    }
}
