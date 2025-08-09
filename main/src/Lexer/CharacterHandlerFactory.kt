package Lexer

object CharacterHandlerFactory {

    private val quoteHandler = QuoteHandler()
    private val separatorHandler = SeparatorHandler()
    private val whiteSpaceHandler = WhiteSpaceHandler()
    private val regularHandler = RegularHandler()

    fun getHandler(type: CharacterType) : CharacterHandler{
        return when (type){
            CharacterType.QUOTE -> quoteHandler
            CharacterType.SEPARATOR -> separatorHandler
            CharacterType.WHITESPACE -> whiteSpaceHandler
            CharacterType.REGULAR -> regularHandler
        }
    }
}