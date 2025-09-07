package src.main.model.tools.interpreter.lexer

import java.io.Reader
import java.io.StringReader

class StringCharSource(private val content: String) : CharSource {
    override fun openReader(): Reader {
        return StringReader(content)
    }
}
