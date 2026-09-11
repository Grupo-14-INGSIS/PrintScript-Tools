package cli.src.main.kotlin.version

import parser.src.main.kotlin.VersionFeatures
import lexer.src.main.kotlin.TokenPlugin
import interpreter.src.main.kotlin.Actions
import interpreter.src.main.kotlin.ActionType
import inputprovider.src.main.kotlin.InputProvider
import tokendata.src.main.kotlin.DataType

interface PrintScriptVersion {
    val name: String
    val features: VersionFeatures
    val keywords: Map<String, DataType>
    val supportedActions: Set<Actions>
    val supportsBlocks: Boolean get() = features.supportsBlocks

    fun createTokenPlugins(): List<TokenPlugin>
    fun createActionHandlers(inputProvider: InputProvider?): Map<Actions, ActionType>
}
