package cli.src.main.kotlin.version

import parser.src.main.kotlin.VersionConfig
import parser.src.main.kotlin.VersionFeatures
import lexer.src.main.kotlin.TokenPlugin
import lexer.src.main.kotlin.TokenPluginFactory
import interpreter.src.main.kotlin.Actions
import interpreter.src.main.kotlin.ActionType
import inputprovider.src.main.kotlin.InputProvider
import interpreter.src.main.kotlin.Interpreter
import tokendata.src.main.kotlin.DataType

object Version10 : PrintScriptVersion {
    override val name: String = "1.0"
    override val features: VersionFeatures = VersionConfig.VERSION_1_0
    override val keywords: Map<String, DataType> = TokenPluginFactory.v10Keywords
    override val supportedActions: Set<Actions> = Interpreter.v10Actions

    override fun createTokenPlugins(): List<TokenPlugin> =
        TokenPluginFactory.createPlugins(keywords)

    override fun createActionHandlers(inputProvider: InputProvider?): Map<Actions, ActionType> =
        Interpreter.defaultV10Handlers
}
