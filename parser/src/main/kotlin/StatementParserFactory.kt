package parser.src.main.kotlin

object StatementParserFactory {

    fun createParsers(features: VersionFeatures, version: String): List<StatementParser> {
        val parsers = mutableListOf<StatementParser>()
        if (features.supportsIfElse) {
            parsers.add(IfStatementParser())
        }
        parsers.add(DeclarationWithAssignmentParser(features, version))
        parsers.add(DeclarationWithoutAssignmentParser(features, version))
        parsers.add(SimpleAssignmentParser())
        parsers.add(ExpressionStatementParser())
        return parsers
    }
}
