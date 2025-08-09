package model.tools.interpreter.common

interface Command {
    fun execute(): Any?
}