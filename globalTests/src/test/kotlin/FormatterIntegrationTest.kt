package globlatests.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import formatter.src.main.kotlin.Formatter
import container.src.main.kotlin.Container
import lexer.src.main.kotlin.Lexer
import java.net.URL
import formatter.src.main.kotlin.formatrule.optional.*

// Helper rápido
fun contents(container: Container): List<String> = container.container.map { it.content }

class FormatterIntegrationTest {

//    @Test
//    fun `Formatter adds spaces around plus operator`(@TempDir tempDir: Path) {
//        val configFile = tempDir.resolve("config.yml").toFile()
//        configFile.writeText(
//            """
//            rules:
//              switch: {}
//              setValue: {}
//            """.trimIndent()
//        )
//
//        val formatter = Formatter("a+b", configFile.absolutePath)
//        val result = formatter.execute()
//
//        // Esperamos "a + b"
//        assertEquals(listOf("a", " ", "+", " ", "b"), contents(result))
//    }

    @Test
    fun `Formatter collapses multiple spaces into one`(@TempDir tempDir: Path) {
        val configFile: URL = this::class.java.getResource("/testInteg.yaml") ?: error("Archivo de configuración no encontrado")

        val formatter = Formatter()
        val lexer = Lexer.from("a    b")
        lexer.split()
        val tokens: Container = lexer.createToken(lexer.list)
        val result = formatter.execute(tokens, configFile)

        // Esperamos "a b"
        assertEquals(listOf("a", " ", "b"), contents(result))
    }

    @Test
    fun `Formatter applies both mandatory rules in combination`(@TempDir tempDir: Path) {
        val configFile: URL = this::class.java.getResource("/testInteg.yaml") ?: error("Archivo de configuración no encontrado")

        // Caso con operador y espacios de más
        val formatter = Formatter()
        val lexer = Lexer.from("a    +b")
        lexer.split()
        val tokens: Container = lexer.createToken(lexer.list)
        val result = formatter.execute(tokens, configFile)

        // Esperamos "a + b"
        assertEquals(listOf("a", " ", "+", " ", "b"), contents(result))
    }

    @Test
    fun `Formatter respects configurable rules when enabled`(@TempDir tempDir: Path) {
        val configFile: URL = this::class.java.getResource("/testInteg2.yaml") ?: error("Archivo de configuración no encontrado")

        val formatter = Formatter()
        val lexer = Lexer.from("a : b")
        lexer.split()
        val tokens: Container = lexer.createToken(lexer.list)
        val result = formatter.execute(tokens, configFile)

        // Dependiendo de cómo implementaste NoSpaceBeforeColonRule,
        // debería eliminar el espacio antes de los `:`
        assertTrue(contents(result).joinToString("").contains("a:"))
    }

//    @Test
//    fun adapterSimulationTest() {
//        val expected = "let something: string=\"a really cool thing\";\n" +
//            "let another_thing: string=\"another really cool thing\";\n" +
//            "let twice_thing: string=\"another really cool thing twice\";\n" +
//            "let third_thing: string=\"another really cool thing three times\";"
//        val input = "let something: string= \"a really cool thing\";\n" +
//            "let another_thing: string =\"another really cool thing\";\n" +
//            "let twice_thing: string = \"another really cool thing twice\";\n" +
//            "let third_thing: string=\"another really cool thing three times\";"
//
//        val lexer = Lexer.from(input)
//        lexer.split()
//        val tokens: Container = lexer.createToken(lexer.list)
//        val formatter = Formatter()
//        var result = formatter.executeOne(tokens, NoSpaceBeforeEqualsRule())
//        result = formatter.executeOne(result, NoSpaceAfterEqualsRule())
//        var actual = ""
//        for (i in 0 until result.size()) {
//            actual += result.get(i)!!.content
//        }
//        assertEquals(expected, actual)
//    }
}
