package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType

class Interpreter(
    override val version: String = "1.0",
    private val inputProvider: InputProvider? = null,
    override val printer: (Any?) -> Unit = ::println,
    private val environment: Environment = Environment()
) : ExecutionContext {

    constructor(version: String, printer: (Any?) -> Unit) : this(
        version = version,
        inputProvider = null,
        printer = printer,
        environment = Environment()
    )

    private val customActionHandlers = mutableMapOf<Actions, ActionType>()
    private val customFunctionActions = mutableMapOf<String, Actions>()
    private val customNodeActions = mutableMapOf<DataType, Actions>()

    private val actionHandlers: Map<Actions, ActionType> = run {
        val v10 = mapOf(
            Actions.ADD to Add,
            Actions.SUBTRACT to Subtract,
            Actions.MULTIPLY to Multiply,
            Actions.DIVIDE to Divide,
            Actions.ASSIGNMENT_TO_EXISTING_VAR to AssignmentToExistingVar,
            Actions.PRINT to Print,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT to VarDeclarationAndAssignment,
            Actions.VAR_DECLARATION_ONLY to VarDeclarationOnly,
            Actions.LITERAL to Literal,
            Actions.BLOCK to Block()
        )

        if (version == "1.1") {
            val v11 = buildMap<Actions, ActionType> {
                inputProvider?.let { put(Actions.READ_INPUT, ReadInput(it)) }
                inputProvider?.let { put(Actions.READ_ENV, ReadEnv(it)) }
                put(Actions.IF_STATEMENT, IfStatement())
                put(Actions.CONST_DECLARATION_AND_ASSIGNMENT, VarDeclarationAndAssignment) // Use the unified handler
            }
            v10 + v11
        } else {
            v10
        }
    }

    fun registerHandler(action: Actions, handler: ActionType) {
        customActionHandlers[action] = handler
    }

    fun registerFunctionAction(name: String, action: Actions) {
        customFunctionActions[name] = action
    }

    fun registerNodeAction(type: DataType, action: Actions) {
        customNodeActions[type] = action
    }

    override fun enterScope() {
        environment.enterScope()
    }

    override fun exitScope() {
        environment.exitScope()
    }

    override fun declareVariable(name: String, value: Any?, type: String) {
        environment.declareVariable(name, value, type)
    }

    override fun assignVariable(name: String, value: Any?) {
        environment.assignVariable(name, value)
    }

    override fun resolveVariable(name: String): Any? {
        return environment.resolveVariable(name)
    }

    override fun resolveVariableType(name: String): String? {
        return environment.resolveVariableType(name)
    }

    override fun declareConstant(name: String, value: Any?, type: String) {
        environment.declareConstant(name, value, type)
    }

    override fun interpret(node: ASTNode): Any? {
        val action = determineAction(node)
        if (!isActionSupportedInVersion(action, version)) {
            throw IllegalArgumentException(
                "Action $action is not supported in PrintScript version $version " +
                    "at line ${node.position.line}, column ${node.position.column}"
            )
        }

        val handler = customActionHandlers[action] ?: actionHandlers[action]
            ?: throw IllegalArgumentException(
                "No handler found for action: $action " +
                    "at line ${node.position.line}, column ${node.position.column}"
            )

        return try {
            handler.interpret(node, this)
        } catch (e: Exception) {
            println(
                "Error during interpretation at line ${node.position.line}, " +
                    "column ${node.position.column}: ${e.message}"
            )
            throw e
        }
    }

    fun determineAction(node: ASTNode): Actions {
        if (node.type != null && customNodeActions.containsKey(node.type)) {
            return customNodeActions[node.type]!!
        }

        return when (node.type) {
            DataType.ADDITION -> Actions.ADD
            DataType.SUBTRACTION -> Actions.SUBTRACT
            DataType.MULTIPLICATION -> Actions.MULTIPLY
            DataType.DIVISION -> Actions.DIVIDE
            DataType.PRINTLN -> Actions.PRINT
            DataType.DECLARATION -> { // Declaration WITH assignment
                if (node.children.firstOrNull()?.type == DataType.CONST_KEYWORD) {
                    Actions.CONST_DECLARATION_AND_ASSIGNMENT
                } else {
                    Actions.VAR_DECLARATION_AND_ASSIGNMENT
                }
            }
            DataType.VAR_DECLARATION_WITHOUT_ASSIGNATION -> Actions.VAR_DECLARATION_ONLY // Declaration WITHOUT assignment
            DataType.ASSIGNATION -> Actions.ASSIGNMENT_TO_EXISTING_VAR
            DataType.IF_STATEMENT -> Actions.IF_STATEMENT
            DataType.BLOCK -> Actions.BLOCK
            DataType.FUNCTION_CALL -> {
                customFunctionActions[node.content] ?: when (node.content) {
                    "println" -> Actions.PRINT
                    "readInput" -> Actions.READ_INPUT
                    "readEnv" -> Actions.READ_ENV
                    else -> throw IllegalArgumentException("Unknown function call: '${node.content}'")
                }
            }
            DataType.IDENTIFIER, DataType.NUMBER_LITERAL, DataType.STRING_LITERAL, DataType.BOOLEAN_LITERAL -> Actions.LITERAL
            else -> throw IllegalArgumentException("Unknown action for node type: '${node.type}'")
        }
    }

    private fun isActionSupportedInVersion(action: Actions, version: String): Boolean {
        if (customActionHandlers.containsKey(action)) {
            return true
        }

        val v10Actions = setOf(
            Actions.ADD,
            Actions.SUBTRACT,
            Actions.MULTIPLY,
            Actions.DIVIDE,
            Actions.ASSIGNMENT_TO_EXISTING_VAR,
            Actions.PRINT,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            Actions.LITERAL,
            Actions.BLOCK,
            Actions.VAR_DECLARATION_ONLY // Changed VAR_DECLARATION to VAR_DECLARATION_ONLY
        )

        val v11OnlyActions = setOf(
            Actions.READ_INPUT,
            Actions.READ_ENV,
            Actions.IF_STATEMENT,
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
        val outputs = mutableListOf<String>()

        for (child in ast.children) {
            val result = interpret(child)
            if (result is String) outputs.add(result)
        }

        return outputs
    }
}
