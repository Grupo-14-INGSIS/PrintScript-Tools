package parser.src.main.kotlin

import ast.src.main.kotlin.ASTNode
import container.src.main.kotlin.Container
import tokendata.src.main.kotlin.DataType
import tokendata.src.main.kotlin.Position

class Grammar {

    private val invalid = ASTNode(DataType.INVALID, "", Position(0, 0), listOf())

    private val pratt = PrattParser()

    fun stmtParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
            return invalid
        }
        // VARIABLE DECLARATION

        return if (isAssignation(tokens)) {
            ASTNode(
                tokens.get(4)!!.type,
                tokens.get(4)!!.content,
                tokens.get(4)!!.position,
                listOf( // ASSIGNATION
                    ASTNode(
                        DataType.DECLARATION,
                        "",
                        tokens.get(1)!!.position,
                        listOf(
                            varParse(tokens.sliceOne(1)),
                            typeParse(tokens.sliceOne(3))
                        )
                    ), // DECLARATION
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
        return ASTNode(
            tokens.get(0)!!.type,
            tokens.get(0)!!.content,
            tokens.get(0)!!.position,
            listOf())
    }

    fun typeParse(tokens: Container): ASTNode {
        return ASTNode(
            tokens.get(0)!!.type,
            tokens.get(0)!!.content,
            tokens.get(0)!!.position,
            listOf())
    }

    /*
    <exp> ::= <var> | <funCall> | <arith> | "(" <exp> ")" | <literal>
     */
    fun expParse(tokens: Container): ASTNode {
        if (tokens.isEmpty()) {
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
            DataType.FUNCTION_CALL,
            tokens.first()!!.content,
            tokens.first()!!.position,
            listOf(
                varParse(tokens.sliceOne(0)), // IDENTIFIER
                expParse(tokens.slice(3, tokens.size() - 1)) // PARAMETERS
            )
        )
    }

    fun funParse(tokens: Container): ASTNode {
        return ASTNode(
            tokens.first()!!.type,
            tokens.first()!!.content,
            tokens.first()!!.position,
            listOf())
    }

    fun litParse(tokens: Container): ASTNode {
        return ASTNode(
            tokens.first()!!.type,
            tokens.first()!!.content,
            tokens.first()!!.position,
            listOf())
    }
}
