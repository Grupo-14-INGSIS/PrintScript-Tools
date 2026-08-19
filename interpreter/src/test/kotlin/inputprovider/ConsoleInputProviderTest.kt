package inputprovider.src.test.kotlin

import org.junit.jupiter.api.Assertions
import inputprovider.src.main.kotlin.ConsoleInputProvider
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

class ConsoleInputProviderTest {

    @Test
    fun `readInput returns user input from stdin`() {
        val input = "Larisa\n"
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)

        val provider = ConsoleInputProvider()
        val result = provider.readInput("Enter name: ")

        Assertions.assertEquals("Larisa", result)
    }

    @Test
    fun `readEnv returns environment variable value`() {
        val provider = ConsoleInputProvider()
        val value = provider.readEnv("PATH") // o cualquier variable conocida

        Assertions.assertNotNull(value)
        assertTrue(value!!.isNotEmpty()) // validación genérica
    }
}
