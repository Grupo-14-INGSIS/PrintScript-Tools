package linter

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Position
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class LinterTest {

    private lateinit var mockRule1: LintRule
    private lateinit var mockRule2: LintRule
    private lateinit var mockASTNode: ASTNode
    private lateinit var linter: Linter

    @BeforeEach
    fun setup() {
        mockRule1 = mockk()
        mockRule2 = mockk()
        mockASTNode = mockk()
        linter = Linter(listOf(mockRule1, mockRule2))
    }

    @Test
    fun `all should return empty list when no rules report errors`() {
        every { mockRule1.apply(mockASTNode) } returns emptyList()
        every { mockRule2.apply(mockASTNode) } returns emptyList()

        val result = linter.all(mockASTNode)

        assertTrue(result.isEmpty())
        verify { mockRule1.apply(mockASTNode) }
        verify { mockRule2.apply(mockASTNode) }
    }

    @Test
    fun `all should return combined errors from all rules`() {
        val error1 = LintError("Error 1", Position(1, 1))
        val error2 = LintError("Error 2", Position(2, 2))
        val error3 = LintError("Error 3", Position(3, 3))

        every { mockRule1.apply(mockASTNode) } returns listOf(error1, error2)
        every { mockRule2.apply(mockASTNode) } returns listOf(error3)

        val result = linter.all(mockASTNode)

        assertEquals(3, result.size)
        assertTrue(result.contains(error1))
        assertTrue(result.contains(error2))
        assertTrue(result.contains(error3))
    }

    @Test
    fun `allPassed should return true when no errors`() {
        every { mockRule1.apply(mockASTNode) } returns emptyList()
        every { mockRule2.apply(mockASTNode) } returns emptyList()

        val result = linter.allPassed(mockASTNode)

        assertTrue(result)
    }

    @Test
    fun `allPassed should return false when there are errors`() {
        val error = LintError("Error", Position(1, 1))
        every { mockRule1.apply(mockASTNode) } returns listOf(error)
        every { mockRule2.apply(mockASTNode) } returns emptyList()

        val result = linter.allPassed(mockASTNode)

        assertFalse(result)
    }

    @Test
    fun `linter with empty rules list should return empty errors`() {
        val emptyLinter = Linter(emptyList())

        val result = emptyLinter.all(mockASTNode)

        assertTrue(result.isEmpty())
    }
}
