package formatter.tests

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import formatter.src.main.kotlin.Formatter
import container.src.main.kotlin.Container
import java.net.URL

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

        val formatter = Formatter("a    b", configFile)
        val result = formatter.execute()

        // Esperamos "a b"
        assertEquals(listOf("a", " ", "b"), contents(result))
    }

    @Test
    fun `Formatter applies both mandatory rules in combination`(@TempDir tempDir: Path) {
        val configFile: URL = this::class.java.getResource("/testInteg.yaml") ?: error("Archivo de configuración no encontrado")

        // Caso con operador y espacios de más
        val formatter = Formatter("a    +b", configFile)
        val result = formatter.execute()

        // Esperamos "a + b"
        assertEquals(listOf("a", " ", "+", " ", "b"), contents(result))
    }

    @Test
    fun `Formatter respects configurable rules when enabled`(@TempDir tempDir: Path) {
        val configFile: URL = this::class.java.getResource("/testInteg2.yaml") ?: error("Archivo de configuración no encontrado")

        val formatter = Formatter("a : b", configFile)
        val result = formatter.execute()

        // Dependiendo de cómo implementaste NoSpaceBeforeColonRule,
        // debería eliminar el espacio antes de los `:`
        assertTrue(contents(result).joinToString("").contains("a:"))
    }
}
