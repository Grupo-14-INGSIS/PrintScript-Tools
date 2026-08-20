package progress.src.main.kotlin

class MultiStepProgress {

    private var currentStep = 0
    private var totalSteps = 0

    fun initialize(steps: Int): Boolean {
        totalSteps = steps
        currentStep = 0
        println("Starting process...")
        return true
    }

    fun startStep(message: String): ProgressIndicator {
        currentStep++
        val progressMessage = "[$currentStep/$totalSteps] $message"
        println(progressMessage)
        val indicator = ProgressIndicator(progressMessage)
        indicator.start()
        return indicator
    }

    fun complete(): Boolean {
        println("Process completed successfully")
        return true
    }
}
