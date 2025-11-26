package linter.src.test.kotlin

import ast.src.main.kotlin.ASTNode
import tokendata.src.main.kotlin.Position
import io.mockk.*
import linter.src.main.kotlin.LintError
import linter.src.main.kotlin.Linter
import linter.src.main.kotlin.LintRule
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
    fun `linter with empty rules list should return empty errors`() {
        val emptyLinter = Linter(emptyList())
        val result = emptyLinter.all(mockASTNode)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `lint should return empty list when no rules report errors for multiple ASTs`() {
        val mockASTNode2 = mockk<ASTNode>()
        every { mockRule1.apply(any()) } returns emptyList()
        every { mockRule2.apply(any()) } returns emptyList()

        val result = linter.lint(listOf(mockASTNode, mockASTNode2))

        assertTrue(result.isEmpty())
        verify { mockRule1.apply(mockASTNode) }
        verify { mockRule1.apply(mockASTNode2) }
        verify { mockRule2.apply(mockASTNode) }
        verify { mockRule2.apply(mockASTNode2) }
    }

    @Test
    fun `lint should return combined errors from all rules for multiple ASTs`() {
        val mockASTNode2 = mockk<ASTNode>()
        val error1 = LintError("Error 1", Position(1, 1))
        val error2 = LintError("Error 2", Position(2, 2))
        val error3 = LintError("Error 3", Position(3, 3))
        val error4 = LintError("Error 4", Position(4, 4))

        every { mockRule1.apply(mockASTNode) } returns listOf(error1)
        every { mockRule2.apply(mockASTNode) } returns listOf(error2)
        every { mockRule1.apply(mockASTNode2) } returns listOf(error3)
        every { mockRule2.apply(mockASTNode2) } returns listOf(error4)

        val result = linter.lint(listOf(mockASTNode, mockASTNode2))

        assertEquals(4, result.size)
        assertTrue(result.containsAll(listOf(error1, error2, error3, error4)))
    }

    @Test
    fun `lint with empty ast list should return empty errors`() {
        val result = linter.lint(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `lintingPassed should return true when no errors are found in multiple ASTs`() {
        val mockASTNode2 = mockk<ASTNode>()
        every { mockRule1.apply(any()) } returns emptyList()
        every { mockRule2.apply(any()) } returns emptyList()

        val result = linter.lintingPassed(listOf(mockASTNode, mockASTNode2))

        assertTrue(result)
    }

    @Test
    fun `lintingPassed should return false when there are errors across multiple ASTs`() {
        val mockASTNode2 = mockk<ASTNode>()
        val error1 = LintError("Error 1", Position(1, 1))

        every { mockRule1.apply(mockASTNode) } returns emptyList()
        every { mockRule2.apply(mockASTNode) } returns emptyList()
        every { mockRule1.apply(mockASTNode2) } returns listOf(error1)
        every { mockRule2.apply(mockASTNode2) } returns emptyList()

        val result = linter.lintingPassed(listOf(mockASTNode, mockASTNode2))

        assertFalse(result)
    }
}
