package interpreter.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType

class Interpreter(
    private val version: String = "1.0",
    private val inputProvider: InputProvider? = null
) {

    private val actionHandlers: Map<Actions, ActionType> = run {
        // PrintScript 1.0 handlers
        val v10 = mapOf(
            Actions.ADD to Add,
            Actions.SUBTRACT to Subtract,
            Actions.MULTIPLY to Multiply,
            Actions.DIVIDE to Divide,
            Actions.ASSIGNMENT_TO_EXISTING_VAR to AssignmentToExistingVar,
            Actions.PRINT to Print,
            Actions.VAR_DECLARATION to VarDeclaration,
            Actions.VAR_DECLARATION_AND_ASSIGNMENT to VarDeclarationAndAssignment,
            Actions.LITERAL to Literal
        )

        if (version == "1.1") {
            val v11 = buildMap<Actions, ActionType> {
                inputProvider?.let { put(Actions.READ_INPUT, ReadInput(it)) }
                inputProvider?.let { put(Actions.READ_ENV, ReadEnv(it)) }
                put(Actions.IF_STATEMENT, IfStatement(this@Interpreter))
                // acá podés seguir sumando más si aparecen
            }
            v10 + v11
        } else {
            v10
        }
    }

    fun interpret(node: ASTNode, action: Actions): Any? {
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
        if (node.type == DataType.NUMBER_LITERAL ||
            node.type == DataType.STRING_LITERAL ||
            node.type == DataType.BOOLEAN_LITERAL
        ) {
            return Actions.LITERAL
        } else {
            return when (node.content) {
                "+" -> Actions.ADD
                "-" -> Actions.SUBTRACT
                "*" -> Actions.MULTIPLY
                "/" -> Actions.DIVIDE
                "println" -> Actions.PRINT
                "var" -> Actions.VAR_DECLARATION
                "=" -> Actions.VAR_DECLARATION_AND_ASSIGNMENT
                "if" -> Actions.IF_STATEMENT
                "readInput" -> Actions.READ_INPUT
                "readEnv" -> Actions.READ_ENV
                else -> throw IllegalArgumentException("Unknown action for token: '${node.content}'")
            }
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
            Actions.VAR_DECLARATION_AND_ASSIGNMENT,
            Actions.LITERAL
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
