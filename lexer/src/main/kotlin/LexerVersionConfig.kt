package lexer.src.main.kotlin

import tokendata.src.main.kotlin.DataType

data class LexerVersionConfig(
    val keywords: Map<String, DataType>,
    val supportsBlocks: Boolean
)

object LexerVersionRegistry {
    private val registry = mutableMapOf<String, LexerVersionConfig>(
        "1.0" to LexerVersionConfig(
            keywords = TokenPluginFactory.v10Keywords,
            supportsBlocks = false
        ),
        "1.1" to LexerVersionConfig(
            keywords = TokenPluginFactory.v11Keywords,
            supportsBlocks = true
        )
    )

    fun register(version: String, config: LexerVersionConfig) {
        registry[version] = config
    }

    fun getConfig(version: String): LexerVersionConfig =
        registry[version] ?: throw IllegalArgumentException("Unsupported version in lexer: $version")

    fun isSupported(version: String): Boolean = registry.containsKey(version)

    fun supportedVersions(): Set<String> = registry.keys
}
