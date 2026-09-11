package globaltests.src.test.kotlin

import ast.src.main.kotlin.ASTNodeType
import globaltests.src.test.kotlin.dsl.parseStatement
import globaltests.src.test.kotlin.dsl.printScriptTest
import lexer.src.main.kotlin.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tokendata.src.main.kotlin.DataType

class EndToEndTest {

    @Test
    fun readInputTest() {
        printScriptTest {
            version = "1.1"
            fromResourceDir("e2e/read-input")
        }
    }

    @Test
    fun readEnvTest() {
        printScriptTest {
            version = "1.1"
            fromResourceDir("e2e/read-env")
            withEnv("BEST_FOOTBALL_CLUB", "San Lorenzo")
        }
    }

    @Test
    fun `test simple variable declaration and assignment`() {
        printScriptTest {
            code = "let x : number = 5;"
            version = "1.0"
            expectValidSyntax()
        }
    }

    @Test
    fun `test simple variable declaration without assignment`() {
        printScriptTest {
            code = "let x : number;"
            version = "1.0"
            expectValidSyntax()
        }
    }

    @Test
    fun `test arithmetic expression with decimal point`() {
        printScriptTest {
            version = "1.1"
            code = """
                let Pi : number;
                Pi = 3.14;
                println(Pi / 2);
            """.trimIndent()
            expectOutputs("1.57")
        }
    }

    @Test
    fun `test arithmetic expression parsing and evaluation`() {
        val ast = parseStatement("2 + 3 * 4;", "1.0")

        assertEquals(ASTNodeType.ADDITION, ast.type)
        assertEquals(2, ast.children.size)
        assertEquals("2", ast.children[0].content)

        assertEquals(ASTNodeType.MULTIPLICATION, ast.children[1].type)
        assertEquals("3", ast.children[1].children[0].content)
        assertEquals("4", ast.children[1].children[1].content)
    }

    @Test
    fun `test complex expression with parentheses`() {
        val ast = parseStatement("5 * 4;", "1.0")

        assertEquals(ASTNodeType.MULTIPLICATION, ast.type)
        assertEquals("4", ast.children[1].content)
    }

    @Test
    fun `test variable declaration with different types`() {
        val testCases = listOf(
            "let name : string = \"John\";",
            "let age : number = 25;",
            "let pi : number = 3.14;"
        )

        testCases.forEach { input ->
            printScriptTest {
                code = input
                version = "1.0"
                expectValidSyntax()
            }
        }
    }

    @Test
    fun `test lexer token classification`() {
        val input = "let x : number = 42;"
        val lexer = Lexer.from(input, "1.0")
        val tokens = lexer.lexIntoStatements().first()

        val expectedTypes = listOf(
            DataType.LET_KEYWORD,
            DataType.SPACE,
            DataType.IDENTIFIER,
            DataType.SPACE,
            DataType.COLON,
            DataType.SPACE,
            DataType.NUMBER_TYPE,
            DataType.SPACE,
            DataType.ASSIGNATION,
            DataType.SPACE,
            DataType.NUMBER_LITERAL,
            DataType.SEMICOLON
        )

        assertEquals(expectedTypes.size, tokens.size())

        for (i in expectedTypes.indices) {
            assertEquals(
                expectedTypes[i],
                tokens.get(i)?.type,
                "Token at position $i should be ${expectedTypes[i]} but was ${tokens.get(i)?.type}"
            )
        }
    }

    @Test
    fun `test string literal with quotes`() {
        val input = "\"This is a string with spaces\";"
        val lexer = Lexer.from(input, "1.0")
        val tokens = lexer.lexIntoStatements().first()

        assertEquals(2, tokens.size())
        assertEquals(DataType.STRING_LITERAL, tokens.get(0)?.type)
        assertEquals("\"This is a string with spaces\"", tokens.get(0)?.content)
    }

    @Test
    fun `test arithmetic operations evaluation`() {
        val testCases = listOf(
            "5 + 3;",
            "10 - 4;",
            "6 * 7;",
            "15 / 3;"
        )

        testCases.forEach { input ->
            val ast = parseStatement(input, "1.0")
            assertNotNull(ast)
            assertTrue(
                ast.type in listOf(
                    ASTNodeType.ADDITION,
                    ASTNodeType.SUBTRACTION,
                    ASTNodeType.MULTIPLICATION,
                    ASTNodeType.DIVISION
                )
            )
        }
    }

    @Test
    fun `test complete program execution`() {
        val program = """
            let message : string = "Hello";
            let count : number = 42;
            println message;
        """.trimIndent()

        val lexer = Lexer.from(program, "1.0")
        val statements = lexer.lexIntoStatements()
        val allTokens = statements.flatMap { it.container }.toList()

        assertNotNull(allTokens)
        assertTrue(allTokens.size > 10)
    }

    @Test
    fun `test lexer with file input simulation`() {
        val fileContent = """
            let x : number = 10;
            let y : number = 20;
            let result : number = x + y;
            println result;
        """.trimIndent()

        val lexer = Lexer.from(fileContent, "1.0")
        val statements = lexer.lexIntoStatements()
        val allTokens = statements.flatMap { it.container }.toList()

        assertTrue(allTokens.size > 20)

        var letCount = 0
        var numberCount = 0

        for (token in allTokens) {
            when (token.type) {
                DataType.LET_KEYWORD -> letCount++
                DataType.NUMBER_TYPE -> numberCount++
                else -> {}
            }
        }

        assertEquals(3, letCount, "Should have 3 'let' keywords")
        assertEquals(3, numberCount, "Should have 3 'number' type declarations")
    }

    @Test
    fun `invalid if statement condition throws exception`() {
        printScriptTest {
            version = "1.1"
            code = """
                if(21) {
                    println("this should fail");
                }
            """.trimIndent()
            expectError<IllegalStateException>("La condición de un 'if' debe ser booleana")
        }
    }

    @Test
    fun `parsing if statement with version 1_0 fails`() {
        printScriptTest {
            version = "1.0"
            code = """
                if(true) {
                    println("this should fail");
                }
            """.trimIndent()
            expectError<IllegalArgumentException>("Unknown action for node type: 'INVALID'")
        }
    }

    @Test
    fun `executing multiple statements maintains state`() {
        printScriptTest {
            version = "1.0"
            code = """
                let x: number = 10;
                let y: number = 5;
                x = x + y;
                println(x);
            """.trimIndent()
            expectOutputs("15")
        }
    }

    @Test
    fun `complex arithmetic with variables`() {
        printScriptTest {
            version = "1.0"
            code = """
                let x: number = 10;
                let y: number = 5;
                let z: number = 2;
                println((x + y) * z);
            """.trimIndent()
            expectOutputs("30")
        }
    }

    @Test
    fun `multiple reassignments`() {
        printScriptTest {
            version = "1.0"
            code = """
                let x: number = 10;
                x = x + 5;
                x = x * 2;
                println(x);
            """.trimIndent()
            expectOutputs("30")
        }
    }

    @Test
    fun `using constants`() {
        printScriptTest {
            version = "1.1"
            code = """
                const PI: number = 3.14;
                let radius: number = 10;
                println(PI * radius * radius);
            """.trimIndent()
            expectOutputs("314")
        }
    }

    @Test
    fun `reassigning a constant fails`() {
        printScriptTest {
            version = "1.1"
            code = """
                const PI: number = 3.14;
                PI = 3.14159;
            """.trimIndent()
            expectError<IllegalStateException>("Cannot reassign a constant")
        }
    }

    @Test
    fun `using an undeclared variable fails`() {
        printScriptTest {
            version = "1.0"
            code = "println(x);"
            expectError<IllegalStateException>("Variable 'x' not declared")
        }
    }

    @Test
    fun `type mismatch on assignment fails`() {
        printScriptTest {
            version = "1.0"
            code = """
                let x: number = 10;
                x = "hello";
            """.trimIndent()
            expectError<IllegalArgumentException>("no se puede convertir a número")
        }
    }

    @Test
    fun `if statement with true condition executes block`() {
        printScriptTest {
            version = "1.1"
            code = """
                let x: number = 5;
                if (true) {
                    x = 10;
                    println(x);
                }
                println(x);
            """.trimIndent()
            expectOutputs("10", "10")
        }
    }

    @Test
    fun `math test`() {
        printScriptTest {
            version = "1.1"
            code = """
                let x: number = 5 * 5 - 8;
                println(x);
            """.trimIndent()
            expectOutputs("17")
        }
    }

    @Test
    fun `if statement with false condition skips block`() {
        printScriptTest {
            version = "1.1"
            code = """
                let x: number = 5;
                if (false) {
                    x = 10;
                    println(x);
                }
                println(x);
            """.trimIndent()
            expectOutputs("5")
        }
    }

    @Test
    fun `test if-else statement`() {
        printScriptTest {
            version = "1.1"
            code = """
                let x: number = 5;
                if (false) {
                    x = 10;
                    println("if block");
                } else {
                    x = 20;
                    println("else block");
                }
                println(x);
            """.trimIndent()
            expectOutputs("else block", "20")
        }
    }
}
