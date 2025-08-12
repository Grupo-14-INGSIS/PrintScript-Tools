package model.structure

enum class DataType {
    PUBLIC_KEYWORD,
    FINAL_KEYWORD,

    LET_KEYWORD,

    IDENTIFIER,

    // Types
    STRING_TYPE,
    NUMBER_TYPE,

    // Literals
    STRING_LITERAL,
    NUMBER_LITERAL,

    // Asignation
    ASSIGNATION,
    DECLARATION,

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