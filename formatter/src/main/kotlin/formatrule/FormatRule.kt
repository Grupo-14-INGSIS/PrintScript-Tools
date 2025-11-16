package formatter.src.main.kotlin.formatrule

import container.src.main.kotlin.Container

interface FormatRule {
    /**Toma un container como arguemtno y lo modifica, devolviendo true.
     False si no puede llevar a cabo la operación
     */
    fun format(source: Container): Container
}
