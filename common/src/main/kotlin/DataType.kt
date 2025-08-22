package common.src.main.kotlin

enum class DataType {

    // Keywords
    LET_KEYWORD,

    // Assignation
    ASSIGNATION,
    DECLARATION,

    // Variables and functions
    IDENTIFIER,

    // Types
    STRING_TYPE,
    NUMBER_TYPE,

    // Literals
    STRING_LITERAL,
    NUMBER_LITERAL,

    // Operations
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    PRINTLN,

    // Functions
    FUNCTION_CALL,

    // Punctuation -> May be useful for Formatter
    SPACE,
    COLON,
    SEMICOLON,
    LINE_BREAK,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,

    INVALID
}
