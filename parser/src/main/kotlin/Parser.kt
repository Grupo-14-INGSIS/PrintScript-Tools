package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import token.src.main.kotlin.Token
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position
import kotlin.jvm.JvmOverloads

class Parser @JvmOverloads constructor(
    private var tokens: Container,
    val version: String = "1.0",
    private val statementParsers: List<StatementParser> = StatementParserFactory.createParsers(
        VersionConfig.getFeatures(version),
        version
    )
) {

    val features: VersionFeatures = VersionConfig.getFeatures(version)
    val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())

    fun parse(): ASTNode {
        val line: Container = format()
        return stmtParse(line)
    }

    private fun format(): Container {
        var output = Container()
        val space = DataType.SPACE
        val lineBreak = DataType.LINE_BREAK
        for (i in 0 until tokens.size()) {
            val tokenType = tokens.get(i)!!.type
            if (tokenType != space && tokenType != lineBreak) {
                output = output.addContainer(tokens.get(i)!!)
            }
        }
        return output
    }

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }

        var tokensToParse = tokens
        if (tokens.last()?.type == DataType.SEMICOLON) {
            tokensToParse = tokens.slice(0, tokens.size() - 1)
        }

        if (tokensToParse.isEmpty()) {
            return invalid
        }

        val statementParser = statementParsers.firstOrNull { it.canParse(tokensToParse) }
        return statementParser?.parse(tokensToParse, this) ?: invalid
    }

    private fun isFunctionCall(tokens: Container): Boolean {
        if (tokens.size() < 3) return false

        val functionName = tokens.get(0)!!.type
        val validFunctions = mutableSetOf(DataType.PRINTLN)

        if (features.supportsIfElse) {
            validFunctions.add(DataType.READ_INPUT)
            validFunctions.add(DataType.READ_ENV)
        }

        return validFunctions.contains(functionName) &&
            tokens.get(1)!!.type == DataType.OPEN_PARENTHESIS &&
            tokens.last()!!.type == DataType.CLOSE_PARENTHESIS
    }

    private fun isArith(tokens: Container): Boolean {
        for (i in 0 until tokens.size()) {
            if (features.operators.containsKey(tokens.get(i)!!.content)) {
                return true
            }
        }
        return false
    }

    private fun isLiteral(tokens: Container): Boolean {
        if (tokens.size() != 1) return false

        val literalTypes = mutableSetOf(
            DataType.NUMBER_LITERAL,
            DataType.STRING_LITERAL
        )

        if (features.supportsBooleans) {
            literalTypes.add(DataType.BOOLEAN_LITERAL)
        }

        return literalTypes.contains(tokens.first()!!.type)
    }

    fun ifStmtParse(tokens: Container): ASTNode {
        val ifKeyword = tokens.get(0)!!

        val conditionStart = findTokenIndex(tokens, DataType.OPEN_PARENTHESIS, 1)
        val conditionEnd = findMatchingClosingParenthesis(tokens, conditionStart)
        val blockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, conditionEnd + 1)
        val blockEnd = findMatchingBrace(tokens, blockStart)

        if (conditionStart == -1 || conditionEnd == -1 || blockStart == -1 || blockEnd == -1) {
            return invalid
        }

        val conditionTokens = tokens.slice(conditionStart + 1, conditionEnd)
        val condition = expParse(conditionTokens)

        if (condition.type == DataType.INVALID) {
            return invalid
        }

        val ifBlockTokens = tokens.slice(blockStart + 1, blockEnd)
        val ifBlock = parseBlock(ifBlockTokens)

        val children = mutableListOf(condition, ifBlock)

        val elseIndex = blockEnd + 1
        if (elseIndex < tokens.size() && tokens.get(elseIndex)!!.type == DataType.ELSE_KEYWORD) {
            val elseBlockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, elseIndex + 1)
            if (elseBlockStart != -1) {
                val elseBlockEnd = findMatchingBrace(tokens, elseBlockStart)
                if (elseBlockEnd != -1) {
                    val elseBlockTokens = tokens.slice(elseBlockStart + 1, elseBlockEnd)
                    val elseBlock = parseBlock(elseBlockTokens)
                    children.add(elseBlock)
                } else {
                    return invalid // else keyword found, but no matching closing brace
                }
            } else {
                return invalid // else keyword found, but no opening brace for its block
            }
        }

        return ASTNode(
            DataType.IF_STATEMENT,
            "if",
            ifKeyword.position,
            children
        )
    }

    fun parseBlock(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return ASTNode(
                DataType.BLOCK,
                "block",
                Position(0, 0),
                emptyList()
            )
        }

        val statements = mutableListOf<ASTNode>()
        var currentStmt = Container()
        var braceDepth = 0

        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)!!

            when (token.type) {
                DataType.OPEN_BRACE -> {
                    braceDepth++
                    currentStmt = currentStmt.addContainer(token)
                }

                DataType.CLOSE_BRACE -> {
                    braceDepth--
                    currentStmt = currentStmt.addContainer(token)
                }

                DataType.SEMICOLON -> {
                    if (braceDepth == 0) {
                        if (!currentStmt.isEmpty()) {
                            val stmt = stmtParse(currentStmt)
                            if (stmt.type != DataType.INVALID) {
                                statements.add(stmt)
                            }
                            currentStmt = Container()
                        }
                    } else {
                        currentStmt = currentStmt.addContainer(token)
                    }
                }

                else -> {
                    currentStmt = currentStmt.addContainer(token)
                }
            }
        }

        if (!currentStmt.isEmpty()) {
            val stmt = stmtParse(currentStmt)
            if (stmt.type != DataType.INVALID) {
                statements.add(stmt)
            }
        }

        return ASTNode(
            DataType.BLOCK,
            "block",
            tokens.get(0)?.position ?: Position(0, 0),
            statements
        )
    }

    fun expParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }

        if (isFunctionCall(tokens)) {
            return parseFunctionCall(tokens)
        }

        if (isArith(tokens)) {
            return arithParse(tokens)
        }

        if (tokens.size() >= 2 &&
            tokens.first()!!.type == DataType.OPEN_PARENTHESIS &&
            tokens.last()!!.type == DataType.CLOSE_PARENTHESIS
        ) {
            return expParse(tokens.slice(1, tokens.size() - 1))
        }

        if (tokens.size() == 1 && tokens.first()!!.type == DataType.IDENTIFIER) {
            return ASTNode(
                DataType.IDENTIFIER,
                tokens.first()!!.content,
                tokens.first()!!.position,
                listOf()
            )
        }

        if (isLiteral(tokens)) {
            return ASTNode(
                tokens.first()!!.type,
                tokens.first()!!.content,
                tokens.first()!!.position,
                listOf()
            )
        }

        return invalid
    }

    private fun parseFunctionCall(tokens: Container): ASTNode {
        val functionToken = tokens.get(0)!!

        val argsTokens = tokens.slice(2, tokens.size() - 1)

        return ASTNode(
            DataType.FUNCTION_CALL,
            functionToken.content,
            functionToken.position,
            if (argsTokens.isEmpty()) emptyList() else listOf(expParse(argsTokens))
        )
    }

    fun findTokenIndex(tokens: Container, type: DataType, startFrom: Int = 0): Int {
        for (i in startFrom until tokens.size()) {
            if (tokens.get(i)!!.type == type) return i
        }
        return -1
    }

    fun findMatchingClosingParenthesis(tokens: Container, openIndex: Int): Int {
        if (openIndex < 0 || openIndex >= tokens.size()) return -1

        var parenCount = 1
        for (i in openIndex + 1 until tokens.size()) {
            when (tokens.get(i)!!.type) {
                DataType.OPEN_PARENTHESIS -> parenCount++
                DataType.CLOSE_PARENTHESIS -> {
                    parenCount--
                    if (parenCount == 0) return i
                }

                else -> {}
            }
        }
        return -1
    }

    fun findMatchingBrace(tokens: Container, openBraceIndex: Int): Int {
        if (openBraceIndex < 0 || openBraceIndex >= tokens.size()) return -1

        var braceCount = 1
        for (i in openBraceIndex + 1 until tokens.size()) {
            when (tokens.get(i)!!.type) {
                DataType.OPEN_BRACE -> braceCount++
                DataType.CLOSE_BRACE -> {
                    braceCount--
                    if (braceCount == 0) return i
                }

                else -> {}
            }
        }
        return -1
    }

    val tokenFactory = PrattTokenFactory(features)
    private var recursionDepth = 0
    private val MAX_RECURSION_DEPTH = 1000

    fun arithParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }
        return shuntingYard(tokens)
    }

    fun processTokens(symbols: List<PrattToken>): List<PrattToken> {
        recursionDepth++
        if (recursionDepth > MAX_RECURSION_DEPTH) {
            println("ERROR: Max recursion depth reached in processTokens")
            return symbols
        }

        if (symbols.size <= 1) return symbols

        val nextOperator = highestPrecedIndex(symbols)

        if (nextOperator == -1) return symbols

        if (nextOperator - 1 < 0 || nextOperator + 1 >= symbols.size) {
            println("ERROR: Operator at invalid position: $nextOperator in list of size ${symbols.size}")
            return symbols
        }

        val newSymbols = associateOperation(symbols, nextOperator)

        if (newSymbols.size >= symbols.size) {
            println("ERROR: List not reducing in processTokens")
            println("Original size: ${symbols.size}, New size: ${newSymbols.size}")
            return symbols
        }

        return processTokens(newSymbols)
    }

    fun associateOperation(symbols: List<PrattToken>, operatorIndex: Int): List<PrattToken> {
        val left = symbols[operatorIndex - 1]
        val right = symbols[operatorIndex + 1]
        val operatorToken = symbols[operatorIndex]

        val associatedToken = operatorToken.associate(listOf(left, right))

        val result = mutableListOf<PrattToken>()

        for (i in 0 until operatorIndex - 1) {
            result.add(symbols[i])
        }

        result.add(associatedToken)

        for (i in operatorIndex + 2 until symbols.size) {
            result.add(symbols[i])
        }

        return result
    }

    fun prattify(tokens: Container): List<PrattToken> {
        val result = mutableListOf<PrattToken>()
        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)
            if (token != null) {
                result.add(tokenFactory.createPrattToken(token))
            }
        }
        return result
    }

    fun highestPrecedIndex(symbols: List<PrattToken>): Int {
        var outputIndex = -1
        var highestPrecedence = -1

        for (i in symbols.indices) {
            val token = symbols[i]
            val currentPrecedence = token.precedence()

            if (currentPrecedence <= 0) continue

            when {
                currentPrecedence > highestPrecedence -> {
                    highestPrecedence = currentPrecedence
                    outputIndex = i
                }
                currentPrecedence == highestPrecedence &&
                    token.associativity() == Association.RIGHT -> {
                    outputIndex = i
                }
            }
        }

        return outputIndex
    }

    fun prattToAST(symbol: PrattToken): ASTNode {
        val children = symbol.allChildren().map { prattToAST(it) }

        return ASTNode(
            symbol.token().type,
            symbol.token().content,
            symbol.token().position,
            children
        )
    }

    private fun getPrecedence(token: Token): Int {
        return when (token.type) {
            DataType.ADDITION -> 1
            DataType.SUBTRACTION -> 1
            DataType.MULTIPLICATION -> 2
            DataType.DIVISION -> 2
            else -> -1
        }
    }

    private fun shuntingYard(tokens: Container): ASTNode {
        val operators = ArrayDeque<Token>()
        val postFix = ArrayDeque<Token>()
        var nextToken: Token
        for (i in 0 until tokens.size()) {
            nextToken = tokens.get(i)!!
            if (nextToken.type == DataType.NUMBER_LITERAL || nextToken.type == DataType.IDENTIFIER ||
                nextToken.type == DataType.STRING_LITERAL
            ) {
                postFix.addLast(nextToken)
            } else {
                if (operators.isEmpty()) {
                    operators.addFirst(nextToken)
                } else {
                    if (nextToken.type == DataType.OPEN_PARENTHESIS) {
                        operators.addFirst(nextToken)
                    } else if (nextToken.type == DataType.CLOSE_PARENTHESIS) {
                        try {
                            while (operators.first().type != DataType.OPEN_PARENTHESIS) {
                                postFix.addLast(operators.removeFirst())
                            }
                        } catch (e: Exception) {
                            return invalid
                        }
                        operators.removeFirst()
                    } else {
                        while (getPrecedence(operators.first()) >= getPrecedence(nextToken)) {
                            postFix.addLast(operators.removeFirst())
                            if (operators.isEmpty()) {
                                break
                            }
                        }
                        operators.addFirst(nextToken)
                    }
                }
            }
        }
        while (operators.isNotEmpty()) {
            postFix.addLast(operators.removeFirst())
        }

        val output = ArrayDeque<ASTNode>()
        var children: List<ASTNode>
        while (postFix.isNotEmpty()) {
            nextToken = postFix.removeFirst()
            if (nextToken.type == DataType.NUMBER_LITERAL || nextToken.type == DataType.IDENTIFIER ||
                nextToken.type == DataType.STRING_LITERAL
            ) {
                children = listOf()
            } else {
                if (output.size < 2) {
                    return invalid
                } else {
                    val right = output.removeFirst()
                    val left = output.removeFirst()
                    children = listOf(left, right)
                }
            }
            output.addFirst(
                ASTNode(
                    nextToken.type,
                    nextToken.content,
                    nextToken.position,
                    children
                )
            )
        }
        return output.first()
    }
}
