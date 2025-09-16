package interpreter.test.kotlin

import ast.src.main.kotlin.ASTNode
import inputprovider.src.main.kotlin.ConsoleInputProvider
import inputprovider.src.main.kotlin.InputProvider
import interpreter.src.main.kotlin.ReadEnv
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Assertions.assertEquals
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class ReadEnvTest {

    @Test
    fun `readEnv devuelve valor real del entorno`() {
        val envVar = "PATH"
        val provider = ConsoleInputProvider()

        assumeTrue(System.getenv(envVar) != null)

        val result = provider.readEnv(envVar)
        assertEquals(System.getenv(envVar), result)
    }

    @Test
    fun `interpreta valor ambiguo como string si no es boolean ni double`() {
        val provider = object : InputProvider {
            override fun readInput(prompt: String): String = error("no usado")
            override fun readEnv(varName: String): String? = "3a.5"
        }

        val readEnv = ReadEnv(provider)

        val node = ASTNode(
            type = DataType.FUNCTION_CALL,
            content = "readEnv",
            position = Position(1, 0),
            children = listOf(
                ASTNode(
                    type = DataType.STRING_LITERAL,
                    content = "\"MY_VAR\"",
                    position = Position(1, 1),
                    children = emptyList()
                )
            )
        )

        val result = readEnv.interpret(node)
        assertEquals("3a.5", result)
    }
}
