package lexer.src.main.kotlin

import container.src.main.kotlin.Container

/**
 * Abstracción encargada de la delimitación y partición de piezas léxicas en sentencias independientes.
 * Separa la responsabilidad sintáctica de segmentación de sentencias del núcleo del Lexer (SoC / SRP).
 */
interface StatementSplitter {
    fun splitIntoStatements(
        pieces: Sequence<String>,
        tokenPlugins: List<TokenPlugin>
    ): Sequence<Container>
}

/**
 * Implementación por defecto que particiona sentencias según delimitadores (punto y coma o bloques con llaves).
 * Es extensible y configurable según la versión del lenguaje (OCP).
 */
class DefaultStatementSplitter(
    val version: String = "1.0"
) : StatementSplitter {

    override fun splitIntoStatements(
        pieces: Sequence<String>,
        tokenPlugins: List<TokenPlugin>
    ): Sequence<Container> = sequence {
        var currentStatementStrings = mutableListOf<String>()
        var braceDepth = 0

        val peekingIterator = PeekingIterator(pieces.iterator())

        while (peekingIterator.hasNext()) {
            val piece = peekingIterator.next()
            currentStatementStrings.add(piece)

            when (piece) {
                "{" -> braceDepth++
                "}" -> braceDepth--
            }

            var shouldFinalize = false

            if (version == "1.1" && piece == "}" && braceDepth == 0) {
                // Lookahead para detectar 'else'
                val bufferedWhitespace = mutableListOf<String>()
                var nextNonBlankPiece: String? = null

                while (peekingIterator.hasNext() && peekingIterator.peek().isBlank()) {
                    bufferedWhitespace.add(peekingIterator.next())
                }

                if (peekingIterator.hasNext()) {
                    nextNonBlankPiece = peekingIterator.peek()
                }

                if (nextNonBlankPiece != "else") {
                    shouldFinalize = true
                } else {
                    currentStatementStrings.addAll(bufferedWhitespace)
                    currentStatementStrings.add(peekingIterator.next()) // Consume 'else'
                }
            } else if (piece == ";" && braceDepth == 0) {
                shouldFinalize = true
            }

            if (shouldFinalize) {
                val statementContainer = TokenFactory.createTokens(currentStatementStrings, tokenPlugins)
                yield(statementContainer)
                currentStatementStrings = mutableListOf()
            }
        }

        if (currentStatementStrings.isNotEmpty()) {
            val meaningfulPieces = currentStatementStrings.filter { it.isNotBlank() }
            if (meaningfulPieces.isNotEmpty()) {
                val lastPiece = meaningfulPieces.last()
                if (lastPiece != ";" && lastPiece != "}") {
                    throw IllegalStateException("Statement must end with a semicolon or closing brace. Remaining: $currentStatementStrings")
                }
                val finalContainer = TokenFactory.createTokens(currentStatementStrings, tokenPlugins)
                if (finalContainer.size() > 0) {
                    yield(finalContainer)
                }
            }
        }
    }
}
