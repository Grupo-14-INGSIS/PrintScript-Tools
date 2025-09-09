package cli.src.main.kotlin

import interpreter.src.main.kotlin.InputProvider
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
// lee desde la terminal


/*la interfaz de inputProvider asi como readEnv y readInput quedan en INterpreter pq contienen logica de interpretacion*/
