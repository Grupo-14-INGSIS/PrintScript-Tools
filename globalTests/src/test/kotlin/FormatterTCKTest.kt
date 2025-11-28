package globaltests.src.test.kotlin

import container.src.main.kotlin.Container
import formatter.src.main.kotlin.Formatter
import lexer.src.main.kotlin.Lexer
import lexer.src.main.kotlin.StringCharSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class FormatterTCKTest {

    private fun runFormatterTckTest(testCasePath: String, version: String = "1.0") {
        val basePath = "../../printscript-tck/src/test/resources/formatter/"
        val sourceFile = File(basePath + testCasePath + "/main.ps")
        val configFile = File(basePath + testCasePath + "/config.json")
        val goldenFile = File(basePath + testCasePath + "/golden.ps")

        val sourceContent = sourceFile.readText()
        val goldenContent = goldenFile.readText()

        val lexer = Lexer(StringCharSource(sourceContent), version)
        val statements = lexer.lexIntoStatements()

        val formatter = Formatter()
        val formattedStatements = formatter.execute(statements, configFile.toURI().toURL())

        val result = statementsToString(formattedStatements)

        // Normalize line endings to avoid OS-specific issues
        val normalizedGolden = goldenContent.replace("\r\n", "\n")
        val normalizedResult = result.replace("\r\n", "\n")

        if (normalizedGolden != normalizedResult) {
            println("--- TEST FAILED: $testCasePath ---")
            println("--- EXPECTED ---")
            println(normalizedGolden)
            println("--- ACTUAL ---")
            println(normalizedResult)
            println("--------------------")
        }

        assertEquals(normalizedGolden, normalizedResult)
    }

    private fun statementsToString(statements: List<Container>): String {
        return statements.joinToString(separator = "") { statement ->
            statement.container.joinToString(separator = "") { it.content }
        }
    }

    // v1.0 tests
    @Test
    fun `tck formatter test assign-no-spacing-surrounding-equals`() {
        runFormatterTckTest("1.0/assign-no-spacing-surrounding-equals")
    }

    @Test
    fun `tck formatter test assign-spacing-surrounding-equals`() {
        runFormatterTckTest("1.0/assign-spacing-surrounding-equals")
    }

    @Test
    fun `tck formatter test enforce-decl-spacing-after-colon`() {
        runFormatterTckTest("1.0/enforce-decl-spacing-after-colon")
    }

    @Test
    fun `tck formatter test enforce-decl-spacing-before-colon`() {
        runFormatterTckTest("1.0/enforce-decl-spacing-before-colon")
    }

    @Test
    fun `tck formatter test enforce-single-space-separation`() {
        runFormatterTckTest("1.0/enforce-single-space-separation")
    }

    @Test
    fun `tck formatter test enforce-space-surrounding-operations`() {
        runFormatterTckTest("1.0/enforce-space-surrounding-operations")
    }

    @Test
    fun `tck formatter test line-break-after-statement-enforced`() {
        runFormatterTckTest("1.0/line-break-after-statement-enforced")
    }

    @Test
    fun `tck formatter test print-0-line-breaks-after`() {
        runFormatterTckTest("1.0/print-0-line-breaks-after")
    }

    @Test
    fun `tck formatter test print-1-line-breaks-after`() {
        runFormatterTckTest("1.0/print-1-line-breaks-after")
    }

    @Test
    fun `tck formatter test print-2-line-breaks-after`() {
        runFormatterTckTest("1.0/print-2-line-breaks-after")
    }

    // v1.1 tests
    @Test
    fun `tck formatter test if-brace-below-line`() {
        runFormatterTckTest("1.1/if-brace-below-line", "1.1")
    }

    @Test
    fun `tck formatter test if-brace-same-line`() {
        runFormatterTckTest("1.1/if-brace-same-line", "1.1")
    }

    @Test
    fun `tck formatter test if-indent-inside-2`() {
        runFormatterTckTest("1.1/if-indent-inside-2", "1.1")
    }
}
