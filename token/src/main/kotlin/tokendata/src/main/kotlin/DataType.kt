package tokendata.src.main.kotlin

enum class DataType {

    // Keywords
    LET_KEYWORD,

    // Keywords PrintScript 1.1
    CONST_KEYWORD,
    IF_KEYWORD,
    ELSE_KEYWORD,

    // Assignation
    ASSIGNATION,
    DECLARATION,
    VAR_DECLARATION_WITHOUT_ASSIGNATION,

    // Variables and functions
    IDENTIFIER,

    // Types
    STRING_TYPE,
    NUMBER_TYPE,

    // Types PrintScript 1.1
    BOOLEAN_TYPE,

    // Literals
    STRING_LITERAL,
    NUMBER_LITERAL,

    // Literals PrintScript 1.1
    BOOLEAN_LITERAL,

    // Operations
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    PRINTLN,

    // Functions
    FUNCTION_CALL,

    // Functions 1.1
    READ_INPUT,
    READ_ENV,

    // Control FLow 1.1
    IF_STATEMENT,

    // Punctuation -> May be useful for Formatter
    SPACE,
    COLON,
    SEMICOLON,
    LINE_BREAK,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,

    // Punctuation 1.1
    OPEN_BRACE,
    CLOSE_BRACE,

    INVALID,
    BLOCK,
    SCRIPT
}
