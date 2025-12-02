package lexer.src.main.kotlin

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

class InputStreamCharSource(private val inputStream: InputStream) : CharSource {
    override fun openReader(): Reader {
        return BufferedReader(InputStreamReader(inputStream))
    }
}
