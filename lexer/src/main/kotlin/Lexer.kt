package src.main.model.tools.interpreter.lexer
import common.src.main.kotlin.Container

class Lexer(val input: String) { //conviene que no se pueda reasignar el input, es lo que recibo y no va a variar

    val list = mutableListOf<String>();

    fun splitString() {
        var state = LexerState()
        input.forEach { char ->
            val characterType = CharacterClassifier.classify(char)
            val handler = CharacterHandlerFactory.getHandler(characterType) //puedo operar sobre la clase porque es un object (singleton)
            state = handler.handle(char, state) // cambio: asignar el nuevo estado retornado
        }

        if(state.currentPiece.isNotEmpty()){
            list.addAll(state.pieces + state.currentPiece) // cambio: agregar directamente la concatenación
        } else {
            list.addAll(state.pieces) // cambio: solo agregar pieces si currentPiece está vacío
        }
    }

    fun createToken(list: List<String>) : Container {

        return TokenFactory.createTokens(list)
    }
}



//hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container