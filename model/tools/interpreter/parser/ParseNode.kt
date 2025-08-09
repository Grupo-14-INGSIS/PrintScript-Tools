package model.tools.interpreter.parser

import model.structure.Token

class ParseNode (
    val token: Token,
    val children: List<Token>
)