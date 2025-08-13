import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import src.main.model.structure.DataType
import src.main.model.tools.interpreter.lexer.Lexer

class IntegrationTest {

    @Test
    fun `test complete variable declaration with let`() {
        val input = "let x: number;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Debug: imprimir tokens generados
        println("Tokens generados: ${container.container.size}")
        container.container.forEach { println("${it.type} - '${it.content}'") }

        assertEquals(7, container.container.size)

        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals("let", get(0).content)

            assertEquals(DataType.SPACE, get(1).type)
            assertEquals(" ", get(1).content)

            assertEquals(DataType.IDENTIFIER, get(2).type)
            assertEquals("x", get(2).content)

            assertEquals(DataType.COLON, get(3).type)
            assertEquals(":", get(3).content)

            assertEquals(DataType.SPACE, get(4).type)
            assertEquals(" ", get(4).content)

            assertEquals(DataType.NUMBER_TYPE, get(5).type) // Corregido: ahora debe ser NUMBER_TYPE
            assertEquals("number", get(5).content)

            assertEquals(DataType.SEMICOLON, get(6).type)
            assertEquals(";", get(6).content)
        }
    }

    @Test
    fun `test variable declaration and assignment with number`() {
        val input = "let x: number = 42;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        assertEquals(11, container.container.size)

        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals(DataType.IDENTIFIER, get(2).type)
            assertEquals(DataType.COLON, get(3).type)
            assertEquals(DataType.NUMBER_TYPE, get(5).type) // Corregido
            assertEquals(DataType.ASSIGNATION, get(7).type)
            assertEquals(DataType.NUMBER_LITERAL, get(9).type)
            assertEquals("42", get(9).content)
            assertEquals(DataType.SEMICOLON, get(10).type)
        }
    }

    @Test
    fun `test variable declaration and assignment with string using double quotes`() {
        val input = "let message: string = \"Hello World\";"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        assertEquals(11, container.container.size)

        with(container.container) {
            assertEquals(DataType.LET_KEYWORD, get(0).type)
            assertEquals(DataType.IDENTIFIER, get(2).type)
            assertEquals("message", get(2).content)
            assertEquals(DataType.STRING_TYPE, get(5).type) // Corregido
            assertEquals(DataType.ASSIGNATION, get(7).type)
            assertEquals(DataType.STRING_LITERAL, get(9).type)
            assertEquals("\"Hello World\"", get(9).content)
        }
    }

    @Test
    fun `test variable declaration and assignment with string using single quotes`() {
        val input = "let message: string = 'Hello World';"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        assertEquals(11, container.container.size)

        with(container.container) {
            assertEquals(DataType.STRING_LITERAL, get(9).type)
            assertEquals("'Hello World'", get(9).content)
        }
    }

    @Test
    fun `test arithmetic expression`() {
        val input = "x + y * 2;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Debug
        println("Arithmetic tokens: ${container.container.size}")
        container.container.forEach { println("${it.type} - '${it.content}'") }

        assertEquals(10, container.container.size) // Ajustado según la salida real. Es 10 y no 9 porque el ; tambien cuenta!!

        with(container.container) {
            assertEquals(DataType.IDENTIFIER, get(0).type)
            assertEquals("x", get(0).content)
            assertEquals(DataType.ADDITION, get(2).type)
            assertEquals(DataType.IDENTIFIER, get(4).type)
            assertEquals("y", get(4).content)
            assertEquals(DataType.MULTIPLICATION, get(6).type)
            assertEquals(DataType.NUMBER_LITERAL, get(8).type)
            assertEquals("2", get(8).content)
        }
    }

    @Test
    fun `test println statement with string`() {
        val input = "println(\"Hello\");"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Debug
        println("Println tokens:")
        container.container.forEach { println("${it.type} - '${it.content}'") }

        // Verificar que println se reconoce correctamente
        val printlnToken = container.container.find { it.content == "println" }
        assertNotNull(printlnToken)
        assertEquals(DataType.PRINTLN, printlnToken?.type)

        // Verificar que el string se tokeniza correctamente
        val stringToken = container.container.find { it.content == "\"Hello\"" }
        assertNotNull(stringToken)
        assertEquals(DataType.STRING_LITERAL, stringToken?.type)
    }

    @Test
    fun `test multiple lines program`() {
        val input = """let x: number = 5;
let y: string = "test";
println(x + 1);"""

        val lexer = Lexer(input)
        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Debug
        println("Multi-line tokens:")
        container.container.forEach { println("${it.type} - '${it.content}'") }

        // Verificar que se reconocen los saltos de línea
        val lineBreaks = container.container.filter { it.type == DataType.LINE_BREAK }
        assertEquals(2, lineBreaks.size)

        // Verificar que se reconocen todas las declaraciones
        val letTokens = container.container.filter { it.type == DataType.LET_KEYWORD }
        assertEquals(2, letTokens.size)

        // Verificar que se reconoce println
        val printlnTokens = container.container.filter { it.type == DataType.PRINTLN }
        assertEquals(1, printlnTokens.size)
    }

    @Test
    fun `test decimal number operations`() {
        val input = "result = 3.14 * 2.5;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        with(container.container) {
            val piToken = find { it.content == "3.14" }
            assertNotNull(piToken)
            assertEquals(DataType.NUMBER_LITERAL, piToken?.type)

            val factorToken = find { it.content == "2.5" }
            assertNotNull(factorToken)
            assertEquals(DataType.NUMBER_LITERAL, factorToken?.type)

            val multiplyToken = find { it.content == "*" }
            assertNotNull(multiplyToken)
            assertEquals(DataType.MULTIPLICATION, multiplyToken?.type)
        }
    }

    @Test
    fun `test string concatenation with plus`() {
        val input = "fullName = firstName + \" \" + lastName;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Verificar que hay múltiples operadores de suma
        val additionTokens = container.container.filter { it.type == DataType.ADDITION }
        assertEquals(2, additionTokens.size)

        // Verificar que el string literal con espacio se tokeniza correctamente
        val spaceStringToken = container.container.find { it.content == "\" \"" }
        assertNotNull(spaceStringToken)
        assertEquals(DataType.STRING_LITERAL, spaceStringToken?.type)
    }

    @Test
    fun `test complex expression with parentheses and mixed types`() {
        val input = "result = (x + 5) * 'factor';"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Debug
        println("Complex expression tokens:")
        container.container.forEach { println("${it.type} - '${it.content}'") }

        // Verificar que los identificadores se reconocen
        val identifiers = container.container.filter { it.type == DataType.IDENTIFIER }
        assertTrue(identifiers.any { it.content == "result" })
        assertTrue(identifiers.any { it.content == "x" })

        // Verificar operadores
        assertTrue(container.container.any { it.type == DataType.ASSIGNATION })
        assertTrue(container.container.any { it.type == DataType.ADDITION })
        assertTrue(container.container.any { it.type == DataType.MULTIPLICATION })

        // Verificar string con comillas simples
        val stringToken = container.container.find { it.content == "'factor'" }
        assertNotNull(stringToken)
        assertEquals(DataType.STRING_LITERAL, stringToken?.type)
    }

    @Test
    fun `test position assignment in tokens`() {
        val input = "let x = 5;"
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Verificar que las posiciones se asignan correctamente
        container.container.forEachIndexed { index, token ->
            assertEquals(index, token.position)
        }
    }

    @Test
    fun `test empty strings and filtering`() {
        val input = "  let   x  =  5  ;  "
        val lexer = Lexer(input)

        lexer.splitString()
        val container = lexer.createToken(lexer.list)

        // Verificar que no hay tokens vacíos
        assertTrue(container.container.none { it.content.isEmpty() })

        // Verificar que todos los espacios se mantienen como tokens
        val spaceTokens = container.container.filter { it.type == DataType.SPACE }
        assertTrue(spaceTokens.isNotEmpty())
    }
}