package lexer.src.main.kotlin

import container.src.main.kotlin.Container

/**
 * Abstracción encargada de la delimitación y partición de piezas léxicas en sentencias independientes.
 * Separa la responsabilidad sintáctica de segmentación de sentencias del núcleo del Lexer (SoC / SRP).
 */
interface StatementSplitter {
    fun splitIntoStatements(
        pieces: Sequence<String>,
        tokenPlugins: List<TokenPlugin>
    ): Sequence<Container>
}
