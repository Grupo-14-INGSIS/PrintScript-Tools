package test.model.tools.interpreter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import src.main.model.tools.interpreter.interpreter.ConsoleInputProvider
import java.io.ByteArrayInputStream

class ConsoleInputProviderTest {

    @Test
    fun `readInput returns user input from stdin`() {
        val input = "Larisa\n"
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)

        val provider = ConsoleInputProvider()
        val result = provider.readInput("Enter name: ")

        assertEquals("Larisa", result)
    }

    @Test
    fun `readEnv returns environment variable value`() {
        val provider = ConsoleInputProvider()
        val value = provider.readEnv("PATH") // o cualquier variable conocida

        assertNotNull(value)
        assertTrue(value!!.isNotEmpty()) // validación genérica
    }
}
