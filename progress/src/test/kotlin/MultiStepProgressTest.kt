package progress.src.test.kotlin

import progress.src.main.kotlin.MultiStepProgress
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class MultiStepProgressTest {

    private lateinit var multiStepProgress: MultiStepProgress // lateint pq no se inicializa al moemento de crear l variable
    private lateinit var outputStream: ByteArrayOutputStream
    private lateinit var originalOut: PrintStream

    @BeforeEach
    fun setUp() {
        multiStepProgress = MultiStepProgress()
        outputStream = ByteArrayOutputStream()
        originalOut = System.out
        System.setOut(PrintStream(outputStream))
    }

    @Test
    fun `initialize should set total steps and print starting message`() {
        // When
        val result = multiStepProgress.initialize(5)

        // Then
        assertTrue(result)
        val output = outputStream.toString()
        Assertions.assertTrue(output.contains("Starting process..."))
    }

    @Test
    fun `startStep should increment current step and return ProgressIndicator`() {
        // Given
        multiStepProgress.initialize(3)

        // When
        val indicator1 = multiStepProgress.startStep("First step")
        val indicator2 = multiStepProgress.startStep("Second step")

        // Then
        Assertions.assertNotNull(indicator1)
        Assertions.assertNotNull(indicator2)
        val output = outputStream.toString()
        Assertions.assertTrue(output.contains("[1/3] First step"))
        Assertions.assertTrue(output.contains("[2/3] Second step"))
    }

    @Test
    fun `complete should print completion message and return true`() {
        // Given
        multiStepProgress.initialize(2)

        // When
        val result = multiStepProgress.complete()

        // Then
        assertTrue(result)
        val output = outputStream.toString()
        Assertions.assertTrue(output.contains("Process completed successfully"))
    }

    @Test
    fun `multiple steps should show correct progress numbering`() {
        // Given
        multiStepProgress.initialize(4)

        // When
        multiStepProgress.startStep("Step one")
        multiStepProgress.startStep("Step two")
        multiStepProgress.startStep("Step three")
        multiStepProgress.startStep("Step four")

        // Then
        val output = outputStream.toString()

        Assertions.assertTrue(output.contains("[1/4] Step one"))
        Assertions.assertTrue(output.contains("[2/4] Step two"))
        Assertions.assertTrue(output.contains("[3/4] Step three"))
        Assertions.assertTrue(output.contains("[4/4] Step four"))
    }
}
