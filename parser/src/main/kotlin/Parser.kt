package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class Parser(
    private var tokens: Container,
    val version: String = "1.0" // es redundante poner private val
) {

    fun parse(): ASTNode {
        val line: Container = format()
        return stmtParse(line)
    }

    private fun format(): Container {
        var output = Container()
        val space = DataType.SPACE
        val semicolon = DataType.SEMICOLON
        for (i in 0 until tokens.size()) {
            if (tokens.get(i)!!.type != space && tokens.get(i)!!.type != semicolon) {
                output = output.addContainer(tokens.get(i)!!)
            }
        }
        return output
    }

    // Grammar

    private val features = VersionConfig.getFeatures(version)
    private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }

        // Check for IF statement first (PrintScript 1.1)
        if (tokens.get(0)!!.type == DataType.IF_KEYWORD) {
            return ifStmtParse(tokens)
        }

        // Variable declaration/assignment
        if (isAssignation(tokens)) {
            val isConst = tokens.get(0)!!.type == DataType.CONST_KEYWORD
            val keyword = if (isConst) DataType.CONST_KEYWORD else DataType.LET_KEYWORD

            return ASTNode(
                DataType.DECLARATION,
                tokens.get(4)!!.content,
                tokens.get(4)!!.position,
                listOf(
                    ASTNode(
                        keyword,
                        tokens.get(0)!!.content,
                        tokens.get(1)!!.position,
                        listOf(
                            varParse(tokens.take(1)),
                            typeParse(tokens.take(3))
                        )
                    ),
                    expParse(tokens.slice(5))
                )
            )
        }

        // Simple assignment (x = value)
        if (isSimpleAssignment(tokens)) {
            return ASTNode(
                DataType.ASSIGNATION,
                "=",
                findAssignationPosition(tokens),
                listOf(
                    varParse(tokens.take(0)),
                    expParse(tokens.slice(2))
                )
            )
        }

        return expParse(tokens)
    }

    // IF statement parsing (PrintScript 1.1)
    fun ifStmtParse(tokens: Container): ASTNode {
        if (!features.supportsIfElse) {
            return invalid
        }

        val ifKeyword = tokens.get(0)!! // "if"
        val conditionStart = findTokenIndex(tokens, DataType.OPEN_PARENTHESIS, 1)
        val conditionEnd = findTokenIndex(tokens, DataType.CLOSE_PARENTHESIS, conditionStart + 1)
        val blockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, conditionEnd + 1)
        val blockEnd = findMatchingBrace(tokens, blockStart)

        if (conditionStart == -1 || conditionEnd == -1 || blockStart == -1 || blockEnd == -1) {
            return invalid
        }

        val condition = expParse(tokens.slice(conditionStart + 1, conditionEnd))
        val ifBlock = parseBlock(tokens.slice(blockStart + 1, blockEnd))

        val children = mutableListOf(condition, ifBlock)

        // Check for else
        val elseKeyword = blockEnd + 1
        if (elseKeyword < tokens.size() && tokens.get(elseKeyword)!!.type == DataType.ELSE_KEYWORD) {
            val elseBlockStart = findTokenIndex(tokens, DataType.OPEN_BRACE, elseKeyword + 1)
            val elseBlockEnd = findMatchingBrace(tokens, elseBlockStart)

            if (elseBlockStart != -1 && elseBlockEnd != -1) {
                val elseBlock = parseBlock(tokens.slice(elseBlockStart + 1, elseBlockEnd))
                children.add(elseBlock)
            }
        }

        return ASTNode(
            DataType.IF_STATEMENT,
            "if",
            ifKeyword.position,
            children
        )
    }

    private fun parseBlock(tokens: Container): ASTNode {
        val statements = mutableListOf<ASTNode>()
        var currentStmt = Container()

        for (i in 0 until tokens.size()) {
            val token = tokens.get(i)!!
            currentStmt = currentStmt.addContainer(token)

            if (token.type == DataType.SEMICOLON) {
                val stmt = stmtParse(currentStmt.slice(0, currentStmt.size() - 1))
                if (stmt.type != DataType.INVALID) {
                    statements.add(stmt)
                }
                currentStmt = Container()
            }
        }

        // Handle last statement if no semicolon
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

    private fun isAssignation(tokens: Container): Boolean {
        if (tokens.size() < 5) return false

        val first = tokens.get(0)!!.type
        val colon = DataType.COLON
        val assignation = DataType.ASSIGNATION

        val validKeywords = if (features.supportsConst) {
            setOf(DataType.LET_KEYWORD, DataType.CONST_KEYWORD)
        } else {
            setOf(DataType.LET_KEYWORD)
        }

        return (
            validKeywords.contains(first) &&
                tokens.get(1)!!.type == DataType.IDENTIFIER &&
                tokens.get(2)!!.type == colon &&
                tokens.get(4)!!.type == assignation
            )
    }

    private fun isSimpleAssignment(tokens: Container): Boolean {
        if (tokens.size() < 3) return false

        return (
            tokens.get(0)!!.type == DataType.IDENTIFIER &&
                tokens.get(1)!!.type == DataType.ASSIGNATION
            )
    }

    private fun findAssignationPosition(tokens: Container): Position {
        for (i in 0 until tokens.size()) {
            if (tokens.get(i)!!.type == DataType.ASSIGNATION) {
                return tokens.get(i)!!.position
            }
        }
        return Position(0, 0)
    }

    fun varParse(tokens: Container): ASTNode {
        return ASTNode(
            DataType.IDENTIFIER,
            tokens.get(0)!!.content,
            tokens.get(0)!!.position,
            listOf()
        )
    }

    fun typeParse(tokens: Container): ASTNode {
        return ASTNode(
            tokens.get(0)!!.type,
            tokens.get(0)!!.content,
            tokens.get(0)!!.position,
            listOf()
        )
    }

    /*
    <exp> ::= <var> | <funCall> | <arith> | "(" <exp> ")" | <literal>
     */
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

        if (tokens.first()!!.type == DataType.PRINTLN) {
            return ASTNode(
                tokens.first()!!.type,
                tokens.first()!!.content,
                tokens.first()!!.position,
                listOf(
                    expParse(tokens.slice(1))
                )
            )
        }

        if (tokens.first()!!.type == DataType.IDENTIFIER) {
            return varParse(tokens)
        }

        if (isLiteral(tokens)) {
            return litParse(tokens)
        }

        if (
            tokens.first()!!.type == DataType.OPEN_PARENTHESIS &&
            tokens.last()!!.type == DataType.CLOSE_PARENTHESIS
        ) {
            return expParse(tokens.slice(1, tokens.size() - 1))
        }

        return invalid
    }

    private fun isFunctionCall(tokens: Container): Boolean {
        if (tokens.size() < 3) return false

        val functionName = tokens.get(0)!!.type
        val validFunctions = setOf(DataType.PRINTLN)

        // Add PrintScript 1.1 functions
        val extendedFunctions = if (version == "1.1") {
            validFunctions + setOf(DataType.READ_INPUT, DataType.READ_ENV)
        } else {
            validFunctions
        }

        return (
            extendedFunctions.contains(functionName) &&
                tokens.get(1)!!.type == DataType.OPEN_PARENTHESIS &&
                tokens.get(tokens.size() - 1)!!.type == DataType.CLOSE_PARENTHESIS
            )
    }

    private fun parseFunctionCall(tokens: Container): ASTNode {
        val functionName = tokens.get(0)!!
        val args = tokens.slice(2, tokens.size() - 1)

        return ASTNode(
            functionName.type,
            functionName.content,
            functionName.position,
            if (args.isEmpty()) emptyList() else listOf(expParse(args))
        )
    }

    private fun isArithSymbol(symbol: String): Boolean {
        return features.operators.containsKey(symbol)
    }

    private fun isArith(tokens: Container): Boolean {
        for (i in 0 until tokens.size()) {
            if (isArithSymbol(tokens.get(i)!!.content)) {
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

    fun litParse(tokens: Container): ASTNode {
        return ASTNode(
            tokens.first()!!.type,
            tokens.first()!!.content,
            tokens.first()!!.position,
            listOf()
        )
    }

    // MÉTODOS AUXILIARES
    private fun findTokenIndex(tokens: Container, type: DataType, startFrom: Int = 0): Int {
        for (i in startFrom until tokens.size()) {
            if (tokens.get(i)!!.type == type) return i
        }
        return -1
    }

    private fun findMatchingBrace(tokens: Container, openBraceIndex: Int): Int {
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

    // PrattParser

    // private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())
    private val tokenFactory = PrattTokenFactory(features)

    fun arithParse(tokens: Container): ASTNode {
        val symbols: List<PrattToken> = prattify(tokens)
        val result = processTokens(symbols)
        return if (result.size == 1) prattToAST(result[0]) else invalid
    }

    private fun processTokens(symbols: List<PrattToken>): List<PrattToken> {
        if (symbols.size <= 1) return symbols

        val nextOperator = highestPrecedIndex(symbols)
        if (nextOperator == -1) return symbols

        val newSymbols = associateOperation(symbols, nextOperator)

        /*
        if (newSymbols.size >= symbols.size) {
            println("ERROR: La lista no se redujo! Posible bucle infinito")
            println("Original: ${symbols.map { it.token().content }}")
            println("Nueva: ${newSymbols.map { it.token().content }}")
            return symbols // Evitar recursión infinita
        }

         */

        return processTokens(newSymbols) // ✅ Recursión inmutable
    }

    private fun associateOperation(symbols: List<PrattToken>, operator: Int): List<PrattToken> {
        if (operator - 1 < 0 || operator + 1 >= symbols.size) return symbols

        val left = symbols[operator - 1]
        val right = symbols[operator + 1]
        val operatorToken = symbols[operator]

        val associatedToken = operatorToken.associate(listOf(left, right))

        val newList = mutableListOf<PrattToken>()
        newList.addAll(symbols)
        newList[operator] = associatedToken

        newList.removeAt(operator + 1)
        newList.removeAt(operator - 1)

        /*
        newList.addAll(symbols.subList(0, operator - 1)) // Antes del operador
        newList.add(associatedToken) // Token asociado
        newList.addAll(symbols.subList(operator + 2, symbols.size)) // Después
         */

        return newList
    }


    private fun prattify(tokens: Container): List<PrattToken> {
        return (0 until tokens.size()).map { i ->
            tokenFactory.createPrattToken(tokens.get(i)!!)
        }
    }

    private fun highestPrecedIndex(symbols: List<PrattToken>): Int {
        var output = -1
        var highestPrecedence = -1

        for (i in symbols.indices) {
            val token = symbols[i]
            when {
                token.precedence() > highestPrecedence -> {
                    highestPrecedence = token.precedence()
                    output = i
                }

                token.precedence() == highestPrecedence &&
                    token.associativity() == Association.RIGHT -> {
                    output = i
                }
            }
        }
        return output
    }

    private fun prattToAST(symbol: PrattToken): ASTNode {
        val children = symbol.allChildren().map { prattToAST(it) }

        return ASTNode(
            symbol.token().type,
            symbol.token().content,
            symbol.token().position,
            children
        )
    }
}
