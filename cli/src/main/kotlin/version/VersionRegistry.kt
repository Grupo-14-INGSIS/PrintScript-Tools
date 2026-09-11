package cli.src.main.kotlin.version

object VersionRegistry {
    private val versions = mutableMapOf<String, PrintScriptVersion>()

    init {
        register(Version10)
        register(Version11)
    }

    fun register(version: PrintScriptVersion) {
        versions[version.name] = version
        parser.src.main.kotlin.VersionConfig.register(version.name, version.features)
        lexer.src.main.kotlin.LexerVersionRegistry.register(
            version.name,
            lexer.src.main.kotlin.LexerVersionConfig(version.keywords, version.supportsBlocks)
        )
        interpreter.src.main.kotlin.Interpreter.registerVersion(
            version.name,
            version.supportedActions
        ) { inputProvider ->
            version.createActionHandlers(inputProvider)
        }
    }

    fun get(versionName: String): PrintScriptVersion =
        versions[versionName] ?: throw IllegalArgumentException(
            "Unsupported version '$versionName'. Supported: ${supportedVersions().joinToString(", ")}"
        )

    fun isSupported(versionName: String): Boolean = versions.containsKey(versionName)

    fun supportedVersions(): Set<String> = versions.keys
}
