package lexer.src.main.kotlin

import container.src.main.kotlin.Container
import java.io.File
import java.io.InputStream
import kotlin.jvm.JvmOverloads

class Lexer @JvmOverloads constructor(
    val source: CharSource,
    val tokenPlugins: List<TokenPlugin>,
    val version: String = "1.0",
    val statementSplitter: StatementSplitter = DefaultStatementSplitter(version)
) {

    @JvmOverloads
    constructor(source: CharSource, version: String = "1.0") : this(
        source = source,
        tokenPlugins = TokenPluginFactory.createPlugins(version),
        version = version,
        statementSplitter = DefaultStatementSplitter(version)
    )

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

    fun lexIntoStatements(): Sequence<Container> {
        return statementSplitter.splitIntoStatements(split(), tokenPlugins)
    }

    companion object {
        fun from(input: Any, version: String = "1.0"): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input), version)
            is File -> Lexer(FileCharSource(input), version)
            is InputStream -> Lexer(InputStreamCharSource(input), version)
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }

        fun from(input: Any, tokenPlugins: List<TokenPlugin>, version: String = "1.0"): Lexer = when (input) {
            is String -> Lexer(StringCharSource(input), tokenPlugins, version)
            is File -> Lexer(FileCharSource(input), tokenPlugins, version)
            is InputStream -> Lexer(InputStreamCharSource(input), tokenPlugins, version)
            else -> throw IllegalArgumentException("Unsupported input type: ${input::class}")
        }
    }
}
