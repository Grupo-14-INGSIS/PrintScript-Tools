import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token
import org.junit.jupiter.api.Assertions.assertEquals
import src.main.model.tools.interpreter.interpreter.Print
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class PrintTest {
    @Test
    fun `print number literal outputs correct value`() {
        val node = ASTNode(
            Token(DataType.PRINTLN, "print", Position(1, 0)),
            listOf(
                ASTNode(Token(DataType.NUMBER_LITERAL, "42", Position(1, 1)), emptyList())
            )
        )

        val output = captureStdOut { Print.interpret(node) }
        assertEquals("42", output.trim())
    }

    @Test
    fun `print string literal outputs correct value`() {
        val node = ASTNode(
            Token(DataType.PRINTLN, "print", Position(1, 0)),
            listOf(
                ASTNode(Token(DataType.STRING_LITERAL, "\"hello\"", Position(1, 1)), emptyList())
            )
        )

        val output = captureStdOut { Print.interpret(node) }
        assertEquals("\"hello\"", output.trim())
    }


    fun captureStdOut(block: () -> Unit): String {
        val stream = ByteArrayOutputStream()
        val original = System.out
        System.setOut(PrintStream(stream))
        block()
        System.setOut(original)
        return stream.toString()
    }
}
