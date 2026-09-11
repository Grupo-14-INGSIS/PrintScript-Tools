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

object Version11 : PrintScriptVersion {
    override val name: String = "1.1"
    override val features: VersionFeatures = VersionConfig.VERSION_1_1
    override val keywords: Map<String, DataType> = TokenPluginFactory.v11Keywords
    override val supportedActions: Set<Actions> = Interpreter.v10Actions + Interpreter.v11OnlyActions

    override fun createTokenPlugins(): List<TokenPlugin> =
        TokenPluginFactory.createPlugins(keywords)

    override fun createActionHandlers(inputProvider: InputProvider?): Map<Actions, ActionType> =
        Interpreter.defaultV10Handlers + Interpreter.createV11Handlers(inputProvider)
}
