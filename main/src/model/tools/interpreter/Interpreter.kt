package model.tools.interpreter

import Interpreter.Actions
import Interpreter.Add
import Interpreter.AssignmentToExistingVar
import Interpreter.Divide
import Interpreter.Multiply
import Interpreter.Print
import Interpreter.Subtract
import Interpreter.VarDeclaration
import Interpreter.VarDeclarationAndAssignment
import model.structure.ASTNode

class Interpreter {
    private val actionHandlers = mapOf(
        Actions.ADD to Add, //sin parentesis pq soo object, no class
        Actions.SUBSTRACT to Subtract,
        Actions.MULTIPLY to Multiply,
        Actions.DIVIDE to Divide,
        Actions.ASSIGNMENT_TO_EXISTING_VAR to AssignmentToExistingVar,
        Actions.DIVIDE to Divide,
        Actions.PRINT to Print,
        Actions.VAR_DECLARATION to VarDeclaration,
        Actions.VAR_DECLARATION_AND_ASSIGNMENT to VarDeclarationAndAssignment,
    )

    fun interpret(node: ASTNode, action: Actions): Any? {
        val handler = actionHandlers[action]
            ?: return false
        handler.interpret(node, action)
        return true
    }
}