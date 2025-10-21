package globlatests.src.test.kotlin

import org.junit.jupiter.api.Assertions.*
import container.src.main.kotlin.Container
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
}
