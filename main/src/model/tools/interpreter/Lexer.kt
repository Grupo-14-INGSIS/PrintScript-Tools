package model.tools.interpreter

import Lexer.CharacterClassifier
import Lexer.CharacterHandlerFactory
import Lexer.LexerState
import Lexer.TokenFactory
import model.structure.Container

class Lexer(val input: String) { //conviene que no se pueda reasignar el input, es lo que recibo y no va a variar

    val list = mutableListOf<String>();

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

        return TokenFactory.createTokens(list)
    }
}



//hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container