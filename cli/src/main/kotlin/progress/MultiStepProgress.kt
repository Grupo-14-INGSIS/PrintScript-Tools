package progress

class MultiStepProgress {

    private var currentStep = 0
    private var totalSteps = 0
    private var activeIndicator: ProgressIndicator? = null

    fun initialize(steps: Int): Boolean {
        totalSteps = steps
        currentStep = 0
        println("Starting process...")
        return true
    }

    fun startStep(message: String): ProgressIndicator {
        activeIndicator?.stop()
        currentStep++
        val progressMessage = "[$currentStep/$totalSteps] $message"
        println(progressMessage)
        val indicator = ProgressIndicator(progressMessage)
        indicator.start()
        activeIndicator = indicator
        return indicator
    }

    fun stop() {
        activeIndicator?.stop()
        activeIndicator = null
    }

    fun fail(errorMessage: String = "Failed") {
        activeIndicator?.fail(errorMessage)
        activeIndicator = null
    }

    fun complete(): Boolean {
        activeIndicator?.stop()
        activeIndicator = null
        println("Process completed successfully")
        return true
    }
}
