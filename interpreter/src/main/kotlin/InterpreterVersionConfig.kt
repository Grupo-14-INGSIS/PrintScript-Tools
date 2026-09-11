package interpreter.src.main.kotlin

import inputprovider.src.main.kotlin.InputProvider

data class InterpreterVersionConfig(
    val supportedActions: Set<Actions>,
    val handlerBuilder: (InputProvider?) -> Map<Actions, ActionType>
)

object InterpreterVersionRegistry {
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

    private val registry: MutableMap<String, InterpreterVersionConfig> = mutableMapOf(
        "1.0" to InterpreterVersionConfig(
            supportedActions = v10Actions,
            handlerBuilder = { defaultV10Handlers }
        ),
        "1.1" to InterpreterVersionConfig(
            supportedActions = v10Actions + v11OnlyActions,
            handlerBuilder = { inputProvider -> defaultV10Handlers + createV11Handlers(inputProvider) }
        )
    )

    fun register(version: String, config: InterpreterVersionConfig) {
        registry[version] = config
    }

    fun registerVersion(
        version: String,
        actions: Set<Actions>,
        handlerBuilder: (InputProvider?) -> Map<Actions, ActionType>
    ) {
        registry[version] = InterpreterVersionConfig(actions, handlerBuilder)
    }

    fun getConfig(version: String): InterpreterVersionConfig =
        registry[version] ?: throw IllegalArgumentException("Unsupported version in interpreter: $version")

    fun getSupportedActions(version: String): Set<Actions> =
        registry[version]?.supportedActions ?: emptySet()

    fun getHandlers(version: String, inputProvider: InputProvider?): Map<Actions, ActionType> =
        registry[version]?.handlerBuilder?.invoke(inputProvider) ?: defaultV10Handlers

    fun isSupported(version: String): Boolean = registry.containsKey(version)

    fun isActionSupported(version: String, action: Actions): Boolean =
        registry[version]?.supportedActions?.contains(action) == true

    fun supportedVersions(): Set<String> = registry.keys
}
