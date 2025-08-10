package model.tools.interpreter

import model.structure.ASTNode
import model.structure.Token
import model.structure.Container

class Parser(private val container: Container) {
    private var currentPosition = 0

    fun parseProgram(): List<ASTNode> {
        val statements = mutableListOf<ASTNode>()
        val tokens = container.getTokens()

        while (currentPosition < tokens.size) {
            val statement = parseStatement(tokens)
            statements.add(statement)
        }

        return statements
    }

    private fun parseStatement(tokens: List<Token>): ASTNode {
        val firstToken = tokens[currentPosition]

        return when (firstToken.content) {
            "let" -> parseDeclaration(tokens)
            "println" -> parsePrintln(tokens)
            else -> parseAssignment(tokens) // x = 5;
        }
    }

    private fun parseDeclaration(tokens: List<Token>): ASTNode {
        consume("let", tokens)
        val varName = getCurrentToken(tokens)
        advance()
        consume(":", tokens)
        val varType = getCurrentToken(tokens)
        advance()

        return if (currentPosition < tokens.size && getCurrentToken(tokens).content == "=") {
            // let x: number = 5;
            consume("=", tokens)
            val value = getCurrentToken(tokens)
            advance()
            consume(";", tokens)

            ASTNode(
                token = Token(null, "DECLARATION_ASSIGNMENT", null),
                children = listOf(varName, varType, value)
            )
        } else {
            // let x: number;
            consume(";", tokens)

            ASTNode(
                token = Token(null, "DECLARATION", null),
                children = listOf(varName, varType)
            )
        }
    }

    private fun parseAssignment(tokens: List<Token>): ASTNode {
        val varName = getCurrentToken(tokens)
        advance()
        consume("=", tokens)
        val value = getCurrentToken(tokens)
        advance()
        consume(";", tokens)

        return ASTNode(
            token = Token(null, "ASSIGNMENT", null),
            children = listOf(varName, value)
        )
    }

    private fun parsePrintln(tokens: List<Token>): ASTNode {
        consume("println", tokens)
        consume("(", tokens)
        val argument = getCurrentToken(tokens)
        advance()
        consume(")", tokens)
        consume(";", tokens)

        return ASTNode(
            token = Token(null, "PRINTLN", null),
            children = listOf(argument)
        )
    }

    private fun getCurrentToken(tokens: List<Token>): Token = tokens[currentPosition]
    private fun advance() { currentPosition++ }

    private fun consume(expected: String, tokens: List<Token>) {
        if (getCurrentToken(tokens).content != expected) {
            throw Exception("Expected '$expected', found '${getCurrentToken(tokens).content}'")
        }
        advance()
    }
}