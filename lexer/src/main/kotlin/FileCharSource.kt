package lexer.src.main.kotlin

import java.io.File
import java.io.Reader

class FileCharSource(private val file: File) : CharSource {
    override fun openReader(): Reader {
        return file.bufferedReader()
    }
}
