import linter.src.main.kotlin.config.ConfigLoader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File


class ConfigLoaderTest {


    @Test
    fun `loadYaml should parse yaml file into map`() {
        // creo un archivo yaml temporal
        val yamlContent = """
           rule1:
             enabled: true
             severity: high
           rule2:
             enabled: false
             severity: low
        """.trimIndent()


        val tempFile = File.createTempFile("test-config", ".yaml")
        tempFile.writeText(yamlContent)


        val loader = ConfigLoader()


        // cargo el YAML
        val result = loader.loadYaml(tempFile.absolutePath)


        // verifico el contendio
        val expected = mapOf(
            "rule1" to mapOf("enabled" to true, "severity" to "high"),
            "rule2" to mapOf("enabled" to false, "severity" to "low")
        )


        assertEquals(expected, result)
    }
}
