package inputprovider.src.main.kotlin

interface InputProvider {

    fun readInput(prompt: String): String
    fun readEnv(varName: String): String?
}
