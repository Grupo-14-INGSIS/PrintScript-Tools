package formatter.src.main.kotlin

import com.sun.tools.javac.parser.Lexer
import common.src.main.kotlin.Token

class Formatter {

    private val config: ConfigLoader = ConfigLoader()

    fun execute(source: String): Int {
        val code: String? = readFile(source)
        if (code == null) {
            // File not found
            return 1
        }

    }

    private fun readFile(file: String): String? {

    }

}