package parser.src.main.kotlin

import common.src.main.kotlin.ASTNode
import common.src.main.kotlin.Container
import common.src.main.kotlin.DataType
import common.src.main.kotlin.Token

class Grammar {

    private val invalid = ASTNode(Token(DataType.INVALID, "", 0), listOf())

    private val pratt = PrattParser()

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }
        // VARIABLE DECLARATION

        return if (isAssignation(tokens)) {
            val declaration = Token(DataType.DECLARATION, "", 0)
            ASTNode(
                tokens.get(4)!!, listOf( // ASSIGNATION
                    ASTNode(declaration, listOf(
                        varParse(tokens.sliceOne(1)), typeParse(tokens.sliceOne(3)))), // DECLARATION
                    expParse(tokens.slice(5)) // EXPRESSION TO ASSOCIATE
                )
            )
        } else {
            expParse(tokens)
        }
    }

    private fun isAssignation(tokens: Container): Boolean {
        val let = DataType.LET_KEYWORD
        val colon = DataType.COLON
        val assignation = DataType.ASSIGNATION
        return (
                tokens.get(0)!!.type == let &&
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

    /*
    <exp> ::= <var> | <funCall> | <arith> | "(" <exp> ")" | <literal>
     */
    fun expParse(tokens: Container): ASTNode {
        if (tokens.isEmpty() ) {
            return invalid
        }
        if (isArith(tokens)) {
            return pratt.arithParse(tokens)
        }
        /*
        if (isFunCall(tokens)) {
            return funcallParse(tokens)
        }
        */
        if (tokens.first()!!.type == DataType.PRINTLN) {
            return ASTNode(tokens.first()!!, listOf(
                expParse(tokens.slice(1))
            ))
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

    private fun isFunCall(tokens: Container): Boolean {
        return (
                tokens.size() >= 3 &&
                        tokens.get(1)!!.type == DataType.OPEN_PARENTHESIS &&
                        tokens.get(tokens.size() - 1)!!.type == DataType.CLOSE_PARENTHESIS
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
                                tokens.first()!!.type == DataType.STRING_LITERAL
                        )
                )
    }

    fun funcallParse(tokens: Container): ASTNode {
        return ASTNode(
            Token(DataType.FUNCTION_CALL, "", 0), listOf(
            varParse(tokens.sliceOne(0)), // IDENTIFIER
            expParse(tokens.slice(3, tokens.size() - 1)) // PARAMETERS
        ))
    }

    fun funParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }

    fun litParse(tokens: Container): ASTNode {
        return ASTNode(tokens.get(0)!!, listOf())
    }

}