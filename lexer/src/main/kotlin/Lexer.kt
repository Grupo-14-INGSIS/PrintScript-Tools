package lexer.src.main.kotlin

import container.src.main.kotlin.Container
import java.io.File
import java.io.InputStream
import kotlin.jvm.JvmOverloads

class Lexer @JvmOverloads constructor(val source: CharSource, val version: String = "1.0") {

    fun split(): Sequence<String> = sequence {
        var state = LexerState()

        val reader = source.openReader()
        val buffer = CharArray(8192) // lotSize estándar

        reader.use {
            var charsRead: Int
            while (reader.read(buffer).also { charsRead = it } != -1) {
                for (i in 0 until charsRead) {
                    val (newState, completedPieces) = classifier(buffer[i], state)
                    completedPieces.forEach { yield(it) }
                    state = newState
                }
            }
        }
        // Yield any remaining piece after the loop finishes
        if (state.currentPiece.isNotEmpty()) {
            yield(state.currentPiece)
        }
    }

    fun classifier(char: Char, state: LexerState): Pair<LexerState, List<String>> {
        val type = CharacterClassifier.classify(char)
        val handler = CharacterHandlerFactory.getHandler(type)
        return handler.handle(char, state)
    }

    fun lexIntoStatements(): Sequence<Container> = sequence {
        var currentStatementStrings = mutableListOf<String>()
        var braceDepth = 0

        val peekingIterator = PeekingIterator(split().iterator())

        while (peekingIterator.hasNext()) {
            val piece = peekingIterator.next()

            currentStatementStrings.add(piece)

            when (piece) {
                "{" -> braceDepth++
                "}" -> braceDepth--
            }

            var shouldFinalize = false

            if (version == "1.1" && piece == "}" && braceDepth == 0) {
                // Peek ahead for 'else'
                val bufferedWhitespace = mutableListOf<String>()
                var nextNonBlankPiece: String? = null

                // Consume and buffer any whitespace after '}'
                while (peekingIterator.hasNext() && peekingIterator.peek().isBlank()) {
                    bufferedWhitespace.add(peekingIterator.next()) // Consume and buffer
                }

                if (peekingIterator.hasNext()) {
                    nextNonBlankPiece = peekingIterator.peek()
                }

                if (nextNonBlankPiece != "else") {
                    shouldFinalize = true // No 'else' found, finalize
                } else {
                    // 'else' found, do NOT finalize this statement yet.
                    // Add buffered whitespace and 'else' to current statement
                    currentStatementStrings.addAll(bufferedWhitespace)
                    currentStatementStrings.add(peekingIterator.next()) // Consume 'else'
                }
            } else if (piece == ";" && braceDepth == 0) {
                shouldFinalize = true
            }

            if (shouldFinalize) {
                val statementContainer = TokenFactory.createTokens(currentStatementStrings, version)
                yield(statementContainer)
                currentStatementStrings = mutableListOf()
            }
        }

        // Handle any remaining pieces after the loop
        if (currentStatementStrings.isNotEmpty()) {
            val meaningfulPieces = currentStatementStrings.filter { it.isNotBlank() }
            if (meaningfulPieces.isNotEmpty()) {
                val lastPiece = meaningfulPieces.last()
                if (lastPiece != ";" && lastPiece != "}") {
                    throw IllegalStateException("Statement must end with a semicolon or closing brace. Remaining: $currentStatementStrings")
                }
                val finalContainer = TokenFactory.createTokens(currentStatementStrings, version)
                if (finalContainer.size() > 0) {
                    yield(finalContainer)
                }
            }
        }
    }

    companion object {
        fun from(input: Any, version: String = "1.0"): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input), version)
            is File -> Lexer(FileCharSource(input), version)
            is InputStream -> Lexer(InputStreamCharSource(input), version)
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }
    }
}
