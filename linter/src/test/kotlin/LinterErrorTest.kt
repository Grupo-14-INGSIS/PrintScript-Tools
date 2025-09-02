package linter

import common.src.main.kotlin.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class LinterErrorTest {

    @Test
    fun `should create LintError with message and position`() {
        val position = Position(5, 10)
        val message = "Test error message"

        val error = LintError(message, position)

        assertEquals(message, error.message)
        assertEquals(position, error.position)
    }

    @Test
    fun `toString should format correctly`() {
        val position = Position(3, 7)
        val message = "Invalid identifier"
        val error = LintError(message, position)

        val result = error.toString()

        assertEquals(
            "[Linter] Row Position(line=3, column=7).line, Column Position(line=3, column=7).column: Invalid identifier",
            result
        )
    }
}
