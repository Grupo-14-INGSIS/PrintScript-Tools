package inputprovider.src.main.kotlin

import java.io.BufferedReader
import java.io.InputStreamReader

class ConsoleInputProvider : InputProvider {
    private val reader = BufferedReader(InputStreamReader(System.`in`))

    override fun readInput(prompt: String): String {
        print(prompt)
        return reader.readLine() ?: ""
    }

    override fun readEnv(varName: String): String? {
        return System.getenv(varName)
    }
}
