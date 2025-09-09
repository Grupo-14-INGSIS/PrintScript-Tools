package intepreter.src.test.kotlin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import interpreter.src.main.kotlin.ActionType
import ast.src.main.kotlin.ASTNode
import interpreter.src.main.kotlin.InputProvider
import interpreter.src.main.kotlin.ReadInput
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import token.src.main.kotlin.Token

class ReadInputTest {

    // FakeInputProvider que implementa todos los métodos requeridos
    private class FakeInputProvider(private val response: String) : InputProvider {
        override fun readInput(prompt: String): String = response
        override fun readEnv(varName: String): String? = null
    }

    // TokenFactory mínimo para evitar errores de construcción
    private fun token(content: String): Token {
        return Token(
            content = content,
            position = Position(0, 0),
            type = DataType.STRING_TYPE // Usar el tipo que tengas definido
        )
    }

    private fun nodeWithPrompt(promptText: String): ASTNode {
        return ASTNode(DataType.STRING_TYPE, "\"$promptText\"", Position(0, 0), children = emptyList())
    }

    @Test
    fun `returns numeric input when convertible`() {
        val inputProvider = FakeInputProvider("42.5")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(DataType.STRING_TYPE, "", Position(0, 0), children = listOf(nodeWithPrompt("Enter a number")))
        val result = action.interpret(node)
        assertEquals(42.5, result)
    }

    @Test
    fun `returns string input when not convertible`() {
        val inputProvider = FakeInputProvider("hello")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(DataType.STRING_TYPE, "", Position(0, 0), children = listOf(nodeWithPrompt("Enter text")))
        val result = action.interpret(node)
        assertEquals("hello", result)
    }

    @Test
    fun `throws when no prompt node is provided`() {
        val inputProvider = FakeInputProvider("ignored")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(DataType.STRING_TYPE, "", Position(0, 0), children = emptyList())
        val exception = assertThrows(IllegalArgumentException::class.java) {
            action.interpret(node)
        }
        assertEquals("readInput requires a prompt argument", exception.message)
    }
}
