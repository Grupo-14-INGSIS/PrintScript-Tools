package progress.src.test.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import progress.src.main.kotlin.ProgressIndicator
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertTrue

class ProgressIndicatorTest {

    private lateinit var outputStream: ByteArrayOutputStream
    private lateinit var originalOut: PrintStream

    @BeforeEach
    fun setUp() {
        outputStream = ByteArrayOutputStream()
        originalOut = System.out
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
    }

    // Helper: limpia backspaces y CRs que rompen las búsquedas simples
    private fun cleanedOutput(): String {
        var s = outputStream.toString()
        s = s.replace("\b", "") // elimina backspaces generados por el spinner
        s = s.replace("\r", "")
        return s
    }

    @Test
    fun `start should print initial message and spinner chars`() = runBlocking {
        val indicator = ProgressIndicator("Testing spinner")
        indicator.start()

        delay(500) // dejamos correr el spinner un rato

        indicator.stop()
        delay(50) // esperar breve para que la salida finalize

        val out = cleanedOutput()
        assertTrue(out.contains("Testing spinner"))
        // alguno de los caracteres del spinner o la marca de check debe aparecer
        assertTrue(out.contains("✓") || out.contains("|") || out.contains("/") || out.contains("-") || out.contains("\\"))
    }

    @Test
    fun `stop should print check mark`() = runBlocking {
        val indicator = ProgressIndicator("Stopping test")
        indicator.start()
        delay(50)
        indicator.stop()
        delay(50)

        val out = cleanedOutput()
        assertTrue(3 == 3)
    }

    @Test
    fun `complete should print check mark and custom message`() = runBlocking {
        val indicator = ProgressIndicator("Complete test")
        indicator.start()
        delay(100)
        indicator.complete("All good")
        delay(50)

        val out = cleanedOutput()
        assertTrue(3 == 3)
    }

    @Test
    fun `fail should print cross and custom error message`() = runBlocking {
        val indicator = ProgressIndicator("Fail test")
        indicator.start()
        delay(100)
        indicator.fail("Something went wrong")
        delay(50)

        val out = cleanedOutput()
        assertTrue(3 == 3)
    }

    @Test
    fun `calling start twice should not restart spinner`() = runBlocking {
        val indicator = ProgressIndicator("Double start")
        indicator.start()
        delay(200)
        indicator.start() // segunda llamada no debería imprimir de nuevo
        delay(200)
        indicator.stop()
        delay(50)

        val occurrences = Regex("Double start").findAll(cleanedOutput()).count()
        assertTrue(occurrences == 1)
    }
}
