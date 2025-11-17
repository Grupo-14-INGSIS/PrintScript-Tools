package formatter.src.test.kotlin


import formatter.src.main.kotlin.ConfigLoader
import formatter.src.main.kotlin.Formatter
import formatter.src.main.kotlin.formatrule.mandatory.*
import formatter.src.main.kotlin.formatrule.optional.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.BeforeEach
import container.src.main.kotlin.Container
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import java.io.File
import java.nio.file.Path


class ConfigLoaderAndFormatterTest {


    @TempDir
    lateinit var tempDir: Path


    private lateinit var formatter: Formatter
    private val pos = Position(0, 0)


    @BeforeEach
    fun setup() {
        formatter = Formatter()
    }


    // ==================== CONFIG LOADER TESTS ====================


    @Test
    fun `ConfigLoader - load all mandatory rules enabled`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: true
mandatory-line-break-after-statement: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(3, rules.size)
        assertTrue(rules.any { it is SpaceBetweenTokensRule })
        assertTrue(rules.any { it is SpaceAroundOperatorRule })
        assertTrue(rules.any { it is LineBreakAfterSemicolonRule })
    }


    @Test
    fun `ConfigLoader - load only some mandatory rules`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: false
mandatory-line-break-after-statement: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(2, rules.size)
        assertTrue(rules.any { it is SpaceBetweenTokensRule })
        assertTrue(rules.any { it is LineBreakAfterSemicolonRule })
        assertFalse(rules.any { it is SpaceAroundOperatorRule })
    }


    @Test
    fun `ConfigLoader - no mandatory rules`() {
        val config = """
mandatory-single-space-separation: false
mandatory-space-surrounding-operations: false
mandatory-line-break-after-statement: false
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(0, rules.size)
    }


    @Test
    fun `ConfigLoader - assign spacing with spaces`() {
        val config = """
enforce-spacing-around-equals: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
        assertTrue(rules.any { it is AssignSpacingRule })
    }


    @Test
    fun `ConfigLoader - assign spacing without spaces`() {
        val config = """
enforce-no-spacing-around-equals: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
        assertTrue(rules.any { it is AssignSpacingRule })
    }


    @Test
    fun `ConfigLoader - space before colon enabled`() {
        val config = """
enforce-spacing-before-colon-in-declaration: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is SpaceBeforeColonRule })
        assertFalse(rules.any { it is NoSpaceBeforeColonRule })
    }


    @Test
    fun `ConfigLoader - no space before colon`() {
        val config = """
enforce-spacing-before-colon-in-declaration: false
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is NoSpaceBeforeColonRule })
        assertFalse(rules.any { it is SpaceBeforeColonRule })
    }


    @Test
    fun `ConfigLoader - space after colon enabled`() {
        val config = """
enforce-spacing-after-colon-in-declaration: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is SpaceAfterColonRule })
        assertFalse(rules.any { it is NoSpaceAfterColonRule })
    }


    @Test
    fun `ConfigLoader - no space after colon`() {
        val config = """
enforce-spacing-after-colon-in-declaration: false
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is NoSpaceAfterColonRule })
        assertFalse(rules.any { it is SpaceAfterColonRule })
    }


    @Test
    fun `ConfigLoader - line breaks after println`() {
        val config = """
line-breaks-after-println: 2
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
        assertTrue(rules.any { it is LineBreakAfterPrintRule })
    }


    @Test
    fun `ConfigLoader - line breaks before println`() {
        val config = """
line-breaks-before-println: 3
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
        assertTrue(rules.any { it is LineBreakBeforePrintRule })
    }


    @Test
    fun `ConfigLoader - indentation with custom size`() {
        val config = """
indent-inside-if: 4
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
        assertTrue(rules.any { it is IndentationRule })
    }


    @Test
    fun `ConfigLoader - if brace same line enabled`() {
        val config = """
if-brace-same-line: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is IfBraceOnSameLineRule })
    }


    @Test
    fun `ConfigLoader - if brace same line disabled`() {
        val config = """
if-brace-same-line: false
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertFalse(rules.any { it is IfBraceOnSameLineRule })
    }


    @Test
    fun `ConfigLoader - if brace below line enabled`() {
        val config = """
if-brace-below-line: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is IfBraceBelowLineRule })
    }


    @Test
    fun `ConfigLoader - complex configuration`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: true
mandatory-line-break-after-statement: true
enforce-spacing-around-equals: true
enforce-spacing-before-colon-in-declaration: true
enforce-spacing-after-colon-in-declaration: false
line-breaks-after-println: 1
line-breaks-before-println: 2
indent-inside-if: 4
if-brace-same-line: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.size >= 9)
    }




    @Test
    fun `ConfigLoader - unknown keys ignored`() {
        val config = """
mandatory-single-space-separation: true
unknown-rule: true
another-unknown: false
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(1, rules.size)
    }


    @Test
    fun `ConfigLoader - extractMandatoryRulesFromConfig`() {
        val loader = ConfigLoader("")
        val config = mapOf(
            "mandatory-single-space-separation" to true,
            "mandatory-space-surrounding-operations" to true,
            "mandatory-line-break-after-statement" to false
        )


        val rules = loader.extractMandatoryRulesFromConfig(config)


        assertEquals(2, rules.size)
    }


    @Test
    fun `ConfigLoader - createConfigurableRules`() {
        val loader = ConfigLoader("")
        val config = mapOf(
            "enforce-spacing-around-equals" to true,
            "line-breaks-after-println" to 2,
            "indent-inside-if" to 4
        )


        val rules = loader.createConfigurableRules(config)


        assertTrue(rules.size >= 3)
    }


    @Test
    fun `ConfigLoader - alternative naming assign-spacing`() {
        val config = """
assign-spacing-surrounding-equals: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is AssignSpacingRule })
    }


    @Test
    fun `ConfigLoader - alternative naming assign-no-spacing`() {
        val config = """
assign-no-spacing-surrounding-equals: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertTrue(rules.any { it is AssignSpacingRule })
    }


    @Test
    fun `ConfigLoader - zero value for println breaks`() {
        val config = """
line-breaks-after-println: 0
line-breaks-before-println: 0
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig()


        assertEquals(2, rules.size)
    }


    @Test
    fun `ConfigLoader - readConfig returns map`() {
        val config = """
key1: true
key2: false
key3: 5
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val configMap = loader.readConfig()


        assertEquals(true, configMap["key1"])
        assertEquals(false, configMap["key2"])
        assertEquals(5, configMap["key3"])
    }


    @Test
    fun `ConfigLoader - loadConfig with version parameter`() {
        val config = """
mandatory-single-space-separation: true
        """.trimIndent()


        val file = createConfigFile(config)
        val loader = ConfigLoader(file.absolutePath)
        val rules = loader.loadConfig("1.1")


        assertEquals(1, rules.size)
    }



    @Test
    fun `Formatter - executeOne with SpaceBetweenTokensRule`() {
        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos)
            )
        )


        val rule = SpaceBetweenTokensRule(enabled = true)
        val result = formatter.executeOne(tokens, rule)


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `Formatter - executeOne with LineBreakAfterSemicolonRule`() {
        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos),
                Token(DataType.SEMICOLON, ";", pos),
                Token(DataType.IDENTIFIER, "y", pos)
            )
        )


        val rule = LineBreakAfterSemicolonRule(enabled = true)
        val result = formatter.executeOne(tokens, rule)


        assertEquals(DataType.LINE_BREAK, result.get(4)?.type)
    }


    @Test
    fun `Formatter - executeOne with disabled rule`() {
        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos)
            )
        )


        val rule = SpaceBetweenTokensRule(enabled = false)
        val result = formatter.executeOne(tokens, rule)


        assertEquals(tokens.size(), result.size())
    }


    @Test
    fun `Formatter - loadRules from config file`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: true
mandatory-line-break-after-statement: true
enforce-spacing-around-equals: true
        """.trimIndent()


        val configFile = createConfigFile(config)
        val rules = formatter.loadRules(configFile.toURI().toURL())


        assertEquals(4, rules.size)
    }


    @Test
    fun `Formatter - execute applies all rules`() {
        val config = """
mandatory-single-space-separation: true
mandatory-line-break-after-statement: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos),
                Token(DataType.SEMICOLON, ";", pos),
                Token(DataType.IDENTIFIER, "y", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `Formatter - execute with empty tokens`() {
        val config = """
mandatory-single-space-separation: true
        """.trimIndent()


        val configFile = createConfigFile(config)
        val tokens = Container(emptyList())
        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(0, result.size())
    }




    @Test
    fun `Formatter - execute with operator spacing`() {
        val config = """
mandatory-space-surrounding-operations: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ADDITION, "+", pos),
                Token(DataType.IDENTIFIER, "y", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.ADDITION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `Formatter - execute with colon spacing`() {
        val config = """
enforce-spacing-before-colon-in-declaration: true
enforce-spacing-after-colon-in-declaration: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.COLON, ":", pos),
                Token(DataType.IDENTIFIER, "Int", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.COLON, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `Formatter - execute with indentation`() {
        val config = """
indent-inside-if: 4
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.OPEN_BRACE, "{", pos),
                Token(DataType.LINE_BREAK, "\n", pos),
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.CLOSE_BRACE, "}", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertTrue(result.size() >= tokens.size())
    }


    @Test
    fun `Formatter - execute with if brace same line`() {
        val config = """
if-brace-same-line: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IF_KEYWORD, "if", pos),
                Token(DataType.OPEN_PARENTHESIS, "(", pos),
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.CLOSE_PARENTHESIS, ")", pos),
                Token(DataType.LINE_BREAK, "\n", pos),
                Token(DataType.OPEN_BRACE, "{", pos),
                Token(DataType.CLOSE_BRACE, "}", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        var foundSpace = false
        for (i in 0 until result.size()) {
            if (result.get(i)?.type == DataType.CLOSE_PARENTHESIS) {
                foundSpace = result.get(i + 1)?.type == DataType.SPACE
                break
            }
        }
        assertTrue(foundSpace)
    }


    @Test
    fun `Formatter - execute with println breaks`() {
        val config = """
line-breaks-after-println: 1
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.PRINTLN, "println", pos),
                Token(DataType.OPEN_PARENTHESIS, "(", pos),
                Token(DataType.STRING_LITERAL, "\"test\"", pos),
                Token(DataType.CLOSE_PARENTHESIS, ")", pos),
                Token(DataType.SEMICOLON, ";", pos),
                Token(DataType.IDENTIFIER, "x", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `Formatter - execute complex config`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: true
mandatory-line-break-after-statement: true
enforce-spacing-around-equals: true
indent-inside-if: 2
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos),
                Token(DataType.ADDITION, "+", pos),
                Token(DataType.NUMBER_LITERAL, "3", pos),
                Token(DataType.SEMICOLON, ";", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertTrue(result.size() > tokens.size())
    }


    @Test
    fun `Formatter - execute with no spacing around equals`() {
        val config = """
enforce-no-spacing-around-equals: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.SPACE, " ", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.SPACE, " ", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.IDENTIFIER, result.get(0)?.type)
        assertEquals(DataType.ASSIGNATION, result.get(1)?.type)
        assertEquals(DataType.NUMBER_LITERAL, result.get(2)?.type)
        assertEquals(3, result.size())
    }


    @Test
    fun `Formatter - execute is idempotent`() {
        val config = """
mandatory-single-space-separation: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.IDENTIFIER, "y", pos)
            )
        )


        val result1 = formatter.execute(tokens, configFile.toURI().toURL())
        val result2 = formatter.execute(result1, configFile.toURI().toURL())


        assertEquals(result1.size(), result2.size())
    }


    @Test
    fun `Formatter - execute with multiplication operator`() {
        val config = """
mandatory-space-surrounding-operations: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "x", pos),
                Token(DataType.MULTIPLICATION, "*", pos),
                Token(DataType.NUMBER_LITERAL, "2", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.MULTIPLICATION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `Formatter - execute with division operator`() {
        val config = """
mandatory-space-surrounding-operations: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.NUMBER_LITERAL, "10", pos),
                Token(DataType.DIVISION, "/", pos),
                Token(DataType.NUMBER_LITERAL, "2", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.DIVISION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `Formatter - execute with subtraction operator`() {
        val config = """
mandatory-space-surrounding-operations: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.NUMBER_LITERAL, "10", pos),
                Token(DataType.SUBTRACTION, "-", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertEquals(DataType.SPACE, result.get(1)?.type)
        assertEquals(DataType.SUBTRACTION, result.get(2)?.type)
        assertEquals(DataType.SPACE, result.get(3)?.type)
    }


    @Test
    fun `Formatter - execute multiple sequential rules`() {
        val config = """
mandatory-single-space-separation: true
mandatory-space-surrounding-operations: true
mandatory-line-break-after-statement: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val tokens = Container(
            listOf(
                Token(DataType.IDENTIFIER, "result", pos),
                Token(DataType.ASSIGNATION, "=", pos),
                Token(DataType.NUMBER_LITERAL, "5", pos),
                Token(DataType.ADDITION, "+", pos),
                Token(DataType.NUMBER_LITERAL, "3", pos),
                Token(DataType.SEMICOLON, ";", pos),
                Token(DataType.PRINTLN, "println", pos)
            )
        )


        val result = formatter.execute(tokens, configFile.toURI().toURL())


        assertTrue(result.size() > tokens.size())


        var hasLineBreakAfterSemicolon = false
        for (i in 0 until result.size() - 1) {
            if (result.get(i)?.type == DataType.SEMICOLON &&
                result.get(i + 1)?.type == DataType.LINE_BREAK
            ) {
                hasLineBreakAfterSemicolon = true
                break
            }
        }
        assertTrue(hasLineBreakAfterSemicolon)
    }


    @Test
    fun `Formatter - execute with all operator types`() {
        val config = """
mandatory-space-surrounding-operations: true
        """.trimIndent()


        val configFile = createConfigFile(config)


        val operators = listOf(
            DataType.ADDITION to "+",
            DataType.SUBTRACTION to "-",
            DataType.MULTIPLICATION to "*",
            DataType.DIVISION to "/"
        )


        operators.forEach { (type, value) ->
            val tokens = Container(
                listOf(
                    Token(DataType.NUMBER_LITERAL, "1", pos),
                    Token(type, value, pos),
                    Token(DataType.NUMBER_LITERAL, "2", pos)
                )
            )


            val result = formatter.execute(tokens, configFile.toURI().toURL())


            assertEquals(DataType.SPACE, result.get(1)?.type)
            assertEquals(type, result.get(2)?.type)
            assertEquals(DataType.SPACE, result.get(3)?.type)
        }
    }


    private fun createConfigFile(content: String): File {
        val file = File(tempDir.toFile(), "test-config-${System.nanoTime()}.yml")
        file.writeText(content)
        return file
    }
}
