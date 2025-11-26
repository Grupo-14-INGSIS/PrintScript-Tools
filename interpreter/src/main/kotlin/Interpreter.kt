package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType

class Interpreter(
    private val version: String = "1.0",
    private val inputProvider: InputProvider? = null
) {
    private val symbolTable = mutableListOf<MutableMap<String, Any?>>(mutableMapOf())
    private val constants = mutableMapOf<String, Any?>()

    private val actionHandlers: Map<Actions, ActionType> = run {
        val v10 = mapOf(
            Actions.ADD to Add,
            Actions.SUBTRACT to Subtract,
            Actions.MULTIPLY to Multiply,
            Actions.DIVIDE to Divide,
            Actions.ASSIGNMENT_TO_EXISTING_VAR to AssignmentToExistingVar,
            Actions.PRINT to Print,
            Actions.VAR_DECLARATION to VarDeclaration,
            Actions.LITERAL to Literal,
            Actions.BLOCK to Block()
        )

        if (version == "1.1") {
            val v11 = buildMap<Actions, ActionType> {
                inputProvider?.let { put(Actions.READ_INPUT, ReadInput(it)) }
                inputProvider?.let { put(Actions.READ_ENV, ReadEnv(it)) }
                put(Actions.IF_STATEMENT, IfStatement())
                put(Actions.VAR_DECLARATION_AND_ASSIGNMENT, VarDeclarationAndAssignment)
                put(Actions.CONST_DECLARATION_AND_ASSIGNMENT, ConstDeclarationAndAssignment)
            }
            v10 + v11
        } else {
            v10
        }
    }

    fun enterScope() {
        symbolTable.add(mutableMapOf())
    }

    fun exitScope() {
        symbolTable.removeAt(symbolTable.lastIndex)
    }

    fun declareVariable(name: String, value: Any?) {
        if (symbolTable.last().containsKey(name)) {
            throw IllegalStateException("Variable '$name' already declared in this scope")
        }
        symbolTable.last()[name] = value
    }

    fun assignVariable(name: String, value: Any?) {
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

    fun declareConstant(name: String, value: Any?) {
        if (constants.containsKey(name)) {
            throw IllegalStateException("Constant '$name' already declared")
        }
        constants[name] = value
    }

    fun interpret(node: ASTNode): Any? {
        val action = determineAction(node)
        if (!isActionSupportedInVersion(action, version)) {
            throw IllegalArgumentException(
                "Action $action is not supported in PrintScript version $version"
            )
        }

        val handler = actionHandlers[action]
            ?: throw IllegalArgumentException("No handler found for action: $action")

        return try {
            handler.interpret(node, this)
        } catch (e: Exception) {
            println("Error during interpretation: ${e.message}")
            throw e
        }
    }

    fun determineAction(node: ASTNode): Actions {
        return when (node.type) {
            DataType.ADDITION -> Actions.ADD
            DataType.SUBTRACTION -> Actions.SUBTRACT
            DataType.MULTIPLICATION -> Actions.MULTIPLY
            DataType.DIVISION -> Actions.DIVIDE
            DataType.PRINTLN -> Actions.PRINT
            DataType.LET_KEYWORD -> Actions.VAR_DECLARATION_AND_ASSIGNMENT
            DataType.CONST_KEYWORD -> Actions.CONST_DECLARATION_AND_ASSIGNMENT
            DataType.ASSIGNATION -> Actions.ASSIGNMENT_TO_EXISTING_VAR
            DataType.IF_STATEMENT -> Actions.IF_STATEMENT
            DataType.DECLARATION -> Actions.VAR_DECLARATION
            DataType.BLOCK -> Actions.BLOCK
            DataType.FUNCTION_CALL -> when (node.content) {
                "readInput" -> Actions.READ_INPUT
                "readEnv" -> Actions.READ_ENV
                else -> throw IllegalArgumentException("Unknown function call: '${node.content}'")
            }
            DataType.IDENTIFIER, DataType.NUMBER_LITERAL, DataType.STRING_LITERAL, DataType.BOOLEAN_LITERAL -> Actions.LITERAL
            else -> throw IllegalArgumentException("Unknown action for node type: '${node.type}'")
        }
    }

    private fun isActionSupportedInVersion(action: Actions, version: String): Boolean {
        val v10Actions = setOf(
            Actions.ADD,
            Actions.SUBTRACT,
            Actions.MULTIPLY,
            Actions.DIVIDE,
            Actions.ASSIGNMENT_TO_EXISTING_VAR,
            Actions.PRINT,
            Actions.VAR_DECLARATION,
            Actions.LITERAL,
            Actions.BLOCK
        )

        val v11OnlyActions = setOf(
            Actions.READ_INPUT,
            Actions.READ_ENV,
            Actions.IF_STATEMENT,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            Actions.CONST_DECLARATION,
            Actions.CONST_DECLARATION_AND_ASSIGNMENT
        )

        return when (version) {
            "1.0" -> action in v10Actions
            "1.1" -> action in v10Actions || action in v11OnlyActions
            else -> false
        }
    }

    fun executeAST(ast: ASTNode): List<String> {
        val output = mutableListOf<String>()
        val printOutput = { message: Any? -> output.add(message.toString()) }

        val originalOut = System.out
        // Redirect println to capture output
        val printStream = object : java.io.PrintStream(originalOut) {
            override fun println(x: Any?) {
                printOutput(x)
            }
        }
        System.setOut(printStream)

        try {
            interpret(ast)
        } finally {
            System.setOut(originalOut)
        }

        return output.toList()
    }
}
