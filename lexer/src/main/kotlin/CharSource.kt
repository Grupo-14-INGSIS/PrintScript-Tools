package src.main.model.tools.interpreter.lexer

import java.io.Reader

interface CharSource {
    fun openReader(): Reader
}
