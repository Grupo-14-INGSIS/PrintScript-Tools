package lexer.src.main.kotlin

import container.src.main.kotlin.Container
import java.io.File
import kotlin.jvm.JvmOverloads

class Lexer @JvmOverloads constructor(val source: CharSource, val version: String = "1.0") {

    var list = listOf<String>()

    fun split(lotSize: Int = 8192) { // buffer size estándar en operaciones de lectura de streams en Java/Kotlin
        var state = LexerState()

        val reader = source.openReader()
        val buffer = CharArray(lotSize)

        reader.use {
            var charsRead: Int
            while (reader.read(buffer).also { charsRead = it } != -1) {
                for (i in 0 until charsRead) {
                    state = classifier(buffer[i], state)
                }
            }
        }

        return finalizeParsing(state)
    }

    fun classifier(char: Char, state: LexerState): LexerState {
        val type = CharacterClassifier.classify(char)
        val handler = CharacterHandlerFactory.getHandler(type)
        return handler.handle(char, state)
    }

    fun finalizeParsing(state: LexerState) {
        val allPieces = if (state.currentPiece.isNotEmpty()) {
            state.pieces + state.currentPiece
        } else {
            state.pieces
        }
        list = allPieces
    }

    fun lexIntoStatements(): List<Container> {
        // Ensure the raw string pieces are generated
        if (this.list.isEmpty()) {
            this.split()
        }

        val statements = mutableListOf<Container>()
        var currentStatementStrings = mutableListOf<String>()
        var braceDepth = 0

        var i = 0
        while (i < this.list.size) {
            val piece = this.list[i]
            currentStatementStrings.add(piece)
            if (version == "1.1") {
                when (piece) {
                    "{" -> braceDepth++
                    "}" -> {
                        braceDepth--
                        if (braceDepth == 0) {
                            val nextNonBlankPiece = findNextNonBlankPiece(i + 1, this.list)
                            if (nextNonBlankPiece == "else") {
                                // Do nothing, continue to consume 'else' and its block
                                // The statement will be finalized after the 'else' block
                            } else {
                                // No 'else' follows (or it's not a v1.1 else), so finalize the statement
                                val statementContainer = TokenFactory.createTokens(currentStatementStrings, version)
                                statements.add(statementContainer)
                                currentStatementStrings = mutableListOf()
                            }
                        }
                    }
                }
            }

            if (piece == ";" && braceDepth == 0) { // Finalize statement if semicolon and braceDepth is 0 (for both versions)
                val statementContainer = TokenFactory.createTokens(currentStatementStrings, version)
                statements.add(statementContainer)
                currentStatementStrings = mutableListOf()
            }
            i++
        }

        // Add any remaining tokens as a final statement
        if (currentStatementStrings.isNotEmpty()) {
            val meaningfulPieces = currentStatementStrings.filter { it.isNotBlank() }
            if (meaningfulPieces.isNotEmpty()) {
                val lastPiece = meaningfulPieces.last()
                if (lastPiece != ";" && lastPiece != "}") {
                    throw IllegalStateException("Statement must end with a semicolon or closing brace.")
                }
                // If the check passes, add the statement
                val finalContainer = TokenFactory.createTokens(currentStatementStrings, version)
                if (finalContainer.size() > 0) {
                    statements.add(finalContainer)
                }
            }
        }
        return statements
    }

    private fun findNextNonBlankPiece(startIndex: Int, pieces: List<String>): String? {
        var j = startIndex
        while (j < pieces.size) {
            if (pieces[j].isNotBlank()) {
                return pieces[j]
            }
            j++
        }
        return null
    }

// puede leer un String gracias a...
    companion object {
        fun from(input: Any, version: String = "1.0"): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input), version)
            is File -> Lexer(FileCharSource(input), version)
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }
    }
}

// hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
// generar lista de palabras separando por espacios
// tomar cada elemento y clasificarlo generanodo un token y guardarlo en container
