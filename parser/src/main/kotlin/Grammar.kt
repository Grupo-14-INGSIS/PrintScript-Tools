package parser.src.main.kotlin

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Position
import common.src.main.kotlin.Token

class Grammar(private val version: String = "1.0") {

    private val invalid = ASTNode(Token(DataType.INVALID, "", Position(0, 0)), listOf())
    private val pratt = PrattParser()

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }

        // CONST support for PrintScript 1.1
        if (isAssignation(tokens)) {
            val declarationType = tokens.get(0)!!.type
            val isConst = declarationType == DataType.CONST_KEYWORD

            val declaration = Token(
                if (isConst) DataType.DECLARATION else DataType.DECLARATION,
                "",
                Position(0, 0)
            )

            return ASTNode(
                tokens.get(4)!!,
                listOf(
                    ASTNode(
                        declaration,
                        listOf(
                            varParse(tokens.sliceOne(1)),
                            typeParse(tokens.sliceOne(3))
                        )
                    ),
                    expParse(tokens.slice(5))
                )
            )
        }

        return expParse(tokens)
    }

    private fun isAssignation(tokens: Container): Boolean {
        val first = tokens.get(0)!!.type
        val colon = DataType.COLON
        val assignation = DataType.ASSIGNATION
        return (
            (first == DataType.LET_KEYWORD || (version == "1.1" && first == DataType.CONST_KEYWORD)) &&
                tokens.get(2)!!.type == colon &&
                tokens.get(4)!!.type == assignation
            )
    }

    fun varParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }

    fun typeParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }

    fun expParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }

        if (isFunctionCall(tokens)) {
            return parseFunctionCall(tokens)
        }

        if (isArith(tokens)) {
            return pratt.arithParse(tokens)
        }

        if (tokens.first()!!.type == DataType.PRINTLN) {
            return ASTNode(
                tokens.first()!!,
                listOf(expParse(tokens.slice(1)))
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
        return (
            tokens.size() >= 3 &&
                tokens.get(1)!!.type == DataType.OPEN_PARENTHESIS &&
                tokens.get(tokens.size() - 1)!!.type == DataType.CLOSE_PARENTHESIS
            )
    }

    private fun parseFunctionCall(tokens: Container): ASTNode {
        val functionName = tokens.get(0)!!
        val args = tokens.slice(2, tokens.size() - 1)

        return ASTNode(
            functionName,
            if (args.isEmpty()) emptyList() else listOf(expParse(args))
        )
    }

    private fun isArithSymbol(symbol: String): Boolean {
        val arithSymbols = listOf(
            "+", "-", "*", "/", "=",
            "++", "--",
            "+=", "-=", "*=", "/=",
            "<", ">", "<=", ">="
        )
        return arithSymbols.contains(symbol)
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
        return (
            tokens.size() == 1 && (
                tokens.first()!!.type == DataType.NUMBER_LITERAL ||
                    tokens.first()!!.type == DataType.STRING_LITERAL ||
                    tokens.first()!!.type == DataType.BOOLEAN_LITERAL
                )
            )
    }

    fun funcallParse(tokens: Container): ASTNode {
        return ASTNode(
            Token(DataType.FUNCTION_CALL, "", Position(0, 0)),
            listOf(
                varParse(tokens.sliceOne(0)),
                expParse(tokens.slice(3, tokens.size() - 1))
            )
        )
    }

    fun funParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }

    fun litParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }
}
