package src.main.tools.interpreter.interpreter

import src.main.structure.ASTNode

object Add : ActionType {
    override fun interpret(node: ASTNode, action: Actions) : Any {
        // Acceder directamente al Token (no .token)
        val left = node.children[0]  // Es Token directamente
        val right = node.children[1] // Es Token directamente

        // Obtener el valor del Token
        val leftValue = left.token.content   // o left.piece, según tu Token
        val rightValue = right.token.content // o right.piece, según tu Token

        // Convertir a números
        val leftNum = leftValue.toDoubleOrNull()
        val rightNum = rightValue.toDoubleOrNull()

        // Si ambos son números: sumar
        if (leftNum != null && rightNum != null) {
            return leftNum + rightNum
        }

        // Si no: concatenar
        return leftValue + rightValue
    }
}

//cubre chaining y mixed chaining