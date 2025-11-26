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

        for (piece in this.list) {
            currentStatementStrings.add(piece)

            if (version == "1.1") { // Version-aware logic
                when (piece) {
                    "{" -> braceDepth++
                    "}" -> {
                        braceDepth--
                        if (braceDepth == 0) {
                            // End of a block statement
                            val statementContainer = TokenFactory.createTokens(currentStatementStrings, version)
                            statements.add(statementContainer)
                            currentStatementStrings = mutableListOf()
                        }
                    }
                }
            }

            if (piece == ";" && braceDepth == 0) {
                // End of a simple statement (works for all versions)
                val statementContainer = TokenFactory.createTokens(currentStatementStrings, version)
                statements.add(statementContainer)
                currentStatementStrings = mutableListOf()
            }
        }

        // Add any remaining tokens as a final statement
        if (currentStatementStrings.isNotEmpty()) {
            val finalContainer = TokenFactory.createTokens(currentStatementStrings, version)
            if (finalContainer.size() > 0) {
                statements.add(finalContainer)
            }
        }

        return statements
    }

// puede leer un String gracias a...
    companion object {
        fun from(input: Any): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input))
            is File -> Lexer(FileCharSource(input))
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }
    }
}

// hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
// generar lista de palabras separando por espacios
// tomar cada elemento y clasificarlo generanodo un token y guardarlo en container
