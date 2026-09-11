package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import ast.src.main.kotlin.ASTNodeType
import inputprovider.src.main.kotlin.InputProvider
import kotlin.jvm.JvmOverloads

class Interpreter(
    private val actionHandlers: Map<Actions, ActionType>,
    private val supportedActions: Set<Actions> = actionHandlers.keys,
    override val printer: (Any?) -> Unit = ::println,
    override val version: String = "custom",
    private val environment: Environment = Environment()
) : ExecutionContext {

    @JvmOverloads
    constructor(
        version: String = "1.0",
        inputProvider: InputProvider? = null,
        printer: (Any?) -> Unit = ::println,
        environment: Environment = Environment()
    ) : this(
        actionHandlers = resolveHandlers(version, inputProvider),
        supportedActions = resolveSupportedActions(version),
        printer = printer,
        version = version,
        environment = environment
    )

    constructor(version: String, printer: (Any?) -> Unit) : this(
        version = version,
        inputProvider = null,
        printer = printer,
        environment = Environment()
    )

    private val customActionHandlers = mutableMapOf<Actions, ActionType>()
    private val customFunctionActions = mutableMapOf<String, Actions>()
    private val customNodeActions = mutableMapOf<ASTNodeType, Actions>()

    fun registerHandler(action: Actions, handler: ActionType) {
        customActionHandlers[action] = handler
    }

    fun registerFunctionAction(name: String, action: Actions) {
        customFunctionActions[name] = action
    }

    fun registerNodeAction(type: ASTNodeType, action: Actions) {
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
            ASTNodeType.ADDITION -> Actions.ADD
            ASTNodeType.SUBTRACTION -> Actions.SUBTRACT
            ASTNodeType.MULTIPLICATION -> Actions.MULTIPLY
            ASTNodeType.DIVISION -> Actions.DIVIDE
            ASTNodeType.PRINTLN -> Actions.PRINT
            ASTNodeType.DECLARATION -> {
                if (node.children.firstOrNull()?.type == ASTNodeType.CONST_KEYWORD) {
                    Actions.CONST_DECLARATION_AND_ASSIGNMENT
                } else {
                    Actions.VAR_DECLARATION_AND_ASSIGNMENT
                }
            }
            ASTNodeType.VAR_DECLARATION_WITHOUT_ASSIGNATION -> Actions.VAR_DECLARATION_ONLY
            ASTNodeType.ASSIGNATION -> Actions.ASSIGNMENT_TO_EXISTING_VAR
            ASTNodeType.IF_STATEMENT -> Actions.IF_STATEMENT
            ASTNodeType.BLOCK -> Actions.BLOCK
            ASTNodeType.FUNCTION_CALL -> {
                customFunctionActions[node.content] ?: when (node.content) {
                    "println" -> Actions.PRINT
                    "readInput" -> Actions.READ_INPUT
                    "readEnv" -> Actions.READ_ENV
                    else -> throw IllegalArgumentException("Unknown function call: '${node.content}'")
                }
            }
            ASTNodeType.IDENTIFIER, ASTNodeType.NUMBER_LITERAL, ASTNodeType.STRING_LITERAL, ASTNodeType.BOOLEAN_LITERAL -> Actions.LITERAL
            else -> throw IllegalArgumentException("Unknown action for node type: '${node.type}'")
        }
    }

    private fun isActionSupportedInVersion(action: Actions, version: String): Boolean {
        if (customActionHandlers.containsKey(action)) {
            return true
        }
        return action in supportedActions
    }

    fun executeAST(ast: ASTNode): List<String> {
        val outputs = mutableListOf<String>()

        for (child in ast.children) {
            val result = interpret(child)
            if (result is String) outputs.add(result)
        }

        return outputs
    }

    companion object {
        val v10Actions: Set<Actions> = setOf(
            Actions.ADD,
            Actions.SUBTRACT,
            Actions.MULTIPLY,
            Actions.DIVIDE,
            Actions.ASSIGNMENT_TO_EXISTING_VAR,
            Actions.PRINT,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            Actions.LITERAL,
            Actions.BLOCK,
            Actions.VAR_DECLARATION_ONLY
        )

        val v11OnlyActions: Set<Actions> = setOf(
            Actions.READ_INPUT,
            Actions.READ_ENV,
            Actions.IF_STATEMENT,
            Actions.CONST_DECLARATION,
            Actions.CONST_DECLARATION_AND_ASSIGNMENT
        )

        val defaultV10Handlers: Map<Actions, ActionType> = mapOf(
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

        fun createV11Handlers(inputProvider: InputProvider?): Map<Actions, ActionType> =
            buildMap {
                inputProvider?.let { put(Actions.READ_INPUT, ReadInput(it)) }
                inputProvider?.let { put(Actions.READ_ENV, ReadEnv(it)) }
                put(Actions.IF_STATEMENT, IfStatement())
                put(Actions.CONST_DECLARATION_AND_ASSIGNMENT, VarDeclarationAndAssignment)
            }

        private val supportedActionsMap = mutableMapOf<String, Set<Actions>>(
            "1.0" to v10Actions,
            "1.1" to v10Actions + v11OnlyActions
        )

        private val handlerBuildersMap = mutableMapOf<String, (InputProvider?) -> Map<Actions, ActionType>>(
            "1.0" to { defaultV10Handlers },
            "1.1" to { inputProvider -> defaultV10Handlers + createV11Handlers(inputProvider) }
        )

        fun registerVersion(version: String, actions: Set<Actions>, handlerBuilder: (InputProvider?) -> Map<Actions, ActionType>) {
            supportedActionsMap[version] = actions
            handlerBuildersMap[version] = handlerBuilder
        }

        private fun resolveSupportedActions(version: String): Set<Actions> =
            supportedActionsMap[version] ?: emptySet()

        private fun resolveHandlers(version: String, inputProvider: InputProvider?): Map<Actions, ActionType> {
            val builder = handlerBuildersMap[version] ?: { defaultV10Handlers }
            return builder(inputProvider)
        }
    }
}
