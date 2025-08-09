package model.tools.interpreter

import Lexer.CharacterClassifier
import Lexer.CharacterHandlerFactory
import Lexer.LexerState
import Lexer.TokenFactory
import model.structure.Container
import model.structure.Token
import model.structure.TokenMap
import model.structure.TokenPattern

class Lexer(val input: String) { //conviene que no se pueda reasignar el input, es lo que recibo y no va a variar

    val list = mutableListOf<String>();
    private val tokenFactory = TokenFactory()

    fun splitString() {
        val state = LexerState()
        input.forEach { char ->
            val characterType = CharacterClassifier.classify(char)
            val handler = CharacterHandlerFactory.getHandler(characterType) //puedo operar sobre la clase porque es un object (singleton)
            handler.handle(char, state)
        }

        if(state.currentPiece.isNotEmpty()){
            state.pieces.add(state.currentPiece.toString())
        }

        list.addAll(state.pieces)
    }

    fun createToken(list: List<String>) : Container {

        return tokenFactory.createTokens(list)
    }
}




//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container