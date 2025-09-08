package test.model.tools.interpreter

import InputProvider
import ReadInput
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import src.main.model.tools.interpreter.interpreter.ActionType
import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token

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
        val promptToken = token("\"$promptText\"")
        return ASTNode(token = promptToken, children = emptyList())
    }

    @Test
    fun `returns numeric input when convertible`() {
        val inputProvider = FakeInputProvider("42.5")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(token = token(""), children = listOf(nodeWithPrompt("Enter a number")))
        val result = action.interpret(node)
        assertEquals(42.5, result)
    }

    @Test
    fun `returns string input when not convertible`() {
        val inputProvider = FakeInputProvider("hello")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(token = token(""), children = listOf(nodeWithPrompt("Enter text")))
        val result = action.interpret(node)
        assertEquals("hello", result)
    }

    @Test
    fun `throws when no prompt node is provided`() {
        val inputProvider = FakeInputProvider("ignored")
        val action: ActionType = ReadInput(inputProvider)
        val node = ASTNode(token = token(""), children = emptyList())
        val exception = assertThrows(IllegalArgumentException::class.java) {
            action.interpret(node)
        }
        assertEquals("readInput requires a prompt argument", exception.message)
    }
}
