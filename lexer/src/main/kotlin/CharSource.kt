package lexer.src.main.kotlin

import java.io.Reader

interface CharSource {
    fun openReader(): Reader
}
