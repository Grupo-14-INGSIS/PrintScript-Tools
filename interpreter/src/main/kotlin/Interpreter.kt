package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider

class Interpreter(
    private val version: String = "1.0",
    private val inputProvider: InputProvider? = null
) {

    private val actionHandlers = mutableMapOf<Actions, ActionType>().apply {
        // PrintScript 1.0 handlers
        put(Actions.ADD, Add)
        put(Actions.SUBTRACT, Subtract)
        put(Actions.MULTIPLY, Multiply)
        put(Actions.DIVIDE, Divide)
        put(Actions.ASSIGNMENT_TO_EXISTING_VAR, AssignmentToExistingVar)
        put(Actions.PRINT, Print)
        put(Actions.VAR_DECLARATION, VarDeclaration)
        put(Actions.VAR_DECLARATION_AND_ASSIGNMENT, VarDeclarationAndAssignment)

        // PrintScript 1.1 handlers (solo si la versión es 1.1)
        if (version == "1.1") {
            inputProvider?.let { ReadInput(it) }?.let { put(Actions.READ_INPUT, it) }
            inputProvider?.let { ReadEnv(it) }?.let { put(Actions.READ_ENV, it) }
            put(Actions.IF_STATEMENT, IfStatement(this@Interpreter))
            // Agregar más handlers según necesites
        }
    }

    fun interpret(node: ASTNode, action: Actions): Any? {
        // Validar que la acción sea compatible con la versión
        if (!isActionSupportedInVersion(action, version)) {
            throw IllegalArgumentException(
                "Action $action is not supported in PrintScript version $version"
            )
        }

        val handler = actionHandlers[action]
            ?: throw IllegalArgumentException("No handler found for action: $action")

        return try {
            handler.interpret(node)
        } catch (e: Exception) {
            println("Error during interpretation: ${e.message}")
            false
        }
    }

    fun determineAction(node: ASTNode): Actions {
        return when (node.content) {
            "+" -> Actions.ADD
            "-" -> Actions.SUBTRACT
            "*" -> Actions.MULTIPLY
            "/" -> Actions.DIVIDE
            "print" -> Actions.PRINT
            "var" -> Actions.VAR_DECLARATION
            "=" -> Actions.VAR_DECLARATION_AND_ASSIGNMENT
            "if" -> Actions.IF_STATEMENT
            "readInput" -> Actions.READ_INPUT
            "readEnv" -> Actions.READ_ENV
            else -> throw IllegalArgumentException("Unknown action for token: '${node.content}'")
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
            Actions.VAR_DECLARATION_AND_ASSIGNMENT
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

    fun executeAST(ast: ASTNode) {
        val queue = ArrayDeque<ASTNode>()
        queue.add(ast)

        while (queue.isNotEmpty()) {
            val currentNode = queue.removeFirst()

            // que accion conlleva el nodo
            val action = try {
                determineAction(currentNode)
            } catch (e: Exception) {
                println("Unknown action for token '${currentNode.content}': ${e.message}")
                continue
            }

            // llevo a cabo esa accion
            try {
                val result = interpret(currentNode, action)
                if (result != null) {
                    println("Result of '${action.name}': $result")
                }
            } catch (e: Exception) {
                println("Error interpreting node '${currentNode.content}': ${e.message}")
            }

            // si existen hijos -> encolarlso
            currentNode.children.forEach { child ->
                queue.add(child)
            }
        }
    }
}
