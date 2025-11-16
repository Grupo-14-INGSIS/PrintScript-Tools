package progress.src.main.kotlin

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean

class ProgressIndicator(private val message: String) {
    private val isRunning = AtomicBoolean(false) // para que trate de forma atomica y evite condicion de carrera
    private var job: Job? = null
    private val spinnerChars = charArrayOf('|', '/', '-', '\\') // los simbolitos que giran
    private var currentSpinner = 0

    fun start() {
        if (isRunning.get()) return

        isRunning.set(true)
        job = CoroutineScope(Dispatchers.IO).launch {
            print("$message ")
            while (isRunning.get()) {
                print("\b${spinnerChars[currentSpinner % spinnerChars.size]}")
                currentSpinner++
                delay(200)
            }
        }
    }

    fun stop() {
        isRunning.set(false)
        job?.cancel()
        print("✓") // agregar \b?
        println()
    }

    fun complete(resultMessage: String = "Done") {
        isRunning.set(false)
        job?.cancel()
        print("✓ $resultMessage")
        println()
    }

    fun fail(errorMessage: String = "Failed") {
        isRunning.set(false)
        job?.cancel()
        print("✗ $errorMessage")
        println()
    }
}
