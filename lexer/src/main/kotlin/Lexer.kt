package src.main.model.tools.interpreter.lexer
import common.src.main.kotlin.Container
import java.io.File

class Lexer(val input: String) { //conviene que no se pueda reasignar el input, es lo que recibo y no va a variar

    constructor(file: File) : this(FileReader().readAsString(file))

    val list = mutableListOf<String>();
    private val fileReader = FileReader()

    fun splitString() {
        var state = LexerState()

        input.forEach { char -> state = classifier(char, state)}

        finalizeParsing(state)
    }

    fun splitStringFromFile(file: File, lotSize: Int = 8192){
        var state = LexerState()

        fileReader.processFile(file, lotSize){char -> state = classifier(char, state)}
        finalizeParsing(state)
    }

    private fun finalizeParsing(state: LexerState) {
        if(state.currentPiece.isNotEmpty()){
            list.addAll(state.pieces + state.currentPiece)
        } else {
            list.addAll(state.pieces)
        }
    }

    private fun classifier(char: Char, state: LexerState): LexerState{
        val characterType = CharacterClassifier.classify(char)
        val handler =CharacterHandlerFactory.getHandler(characterType) //puedo operar sobre la clase porque es un object (singleton)
        return handler.handle(char, state)
    }

    fun createToken(list: List<String>) : Container {

        return TokenFactory.createTokens(list)
    }

    companion object { //companion object tiene que ir dentro de la clase
        fun fromFile(file: File): Lexer = Lexer(file)
        fun fromFilePath(path: String): Lexer = Lexer(File(path))
    }
}

//hace lo MISMO que la version anterior, pero ahora prgormao en el mismo dominio, oculto codigo y es extensible
//generar lista de palabras separando por espacios
//tomar cada elemento y clasificarlo generanodo un token y guardarlo en container