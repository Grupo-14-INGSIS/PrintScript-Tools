package parser.src.main.kotlin

class VersionConfig {
    companion object {
        val VERSION_1_0 = VersionFeatures(
            keywords = setOf("let"),
            types = setOf("string", "number"),
            functions = setOf("println"),
            operators = mapOf(
                "*" to 2,
                "/" to 2,
                "+" to 1,
                "-" to 1
            ),
            associations = mapOf(
                "*" to Association.LEFT,
                "/" to Association.LEFT,
                "+" to Association.LEFT,
                "-" to Association.LEFT
            )
        )

        val VERSION_1_1 = VersionFeatures(
            keywords = setOf("let", "const", "if", "else"),
            types = setOf("string", "number", "boolean"),
            functions = setOf("println", "readInput", "readEnv"),
            operators = mapOf(
                "*" to 2,
                "/" to 2,
                "+" to 1,
                "-" to 1
            ),
            associations = mapOf(
                "*" to Association.LEFT,
                "/" to Association.LEFT,
                "+" to Association.LEFT,
                "-" to Association.LEFT
            ),
            supportsConst = true,
            supportsIfElse = true,
            supportsBlocks = true,
            supportsBooleans = true
        )

        private val registry = mutableMapOf<String, VersionFeatures>(
            "1.0" to VERSION_1_0,
            "1.1" to VERSION_1_1
        )

        fun register(version: String, features: VersionFeatures) {
            registry[version] = features
        }

        fun getFeatures(version: String): VersionFeatures {
            return registry[version] ?: throw IllegalArgumentException("Unsupported version: $version")
        }

        fun isSupported(version: String): Boolean = registry.containsKey(version)

        fun supportedVersions(): Set<String> = registry.keys
    }
}

