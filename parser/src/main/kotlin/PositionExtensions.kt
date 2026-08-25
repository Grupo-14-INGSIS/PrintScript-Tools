package parser.src.main.kotlin

import ast.src.main.kotlin.Position as AstPosition
import tokendata.src.main.kotlin.Position as TokenPosition

fun TokenPosition.toAstPosition(): AstPosition = AstPosition(this.line, this.column)

fun TokenPosition?.toAstPositionOrDefault(defaultLine: Int = 0, defaultColumn: Int = 0): AstPosition =
    this?.toAstPosition() ?: AstPosition(defaultLine, defaultColumn)
