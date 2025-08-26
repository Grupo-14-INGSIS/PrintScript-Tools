package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceAroundOperatorRule
import formatter.src.main.kotlin.formatrule.mandatory.SpaceBetweenTokensRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterColonRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceBeforeEqualsRule
import formatter.src.main.kotlin.formatrule.optional.NoSpaceAfterEqualsRule
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(
    val configFile: String
) {

    /*
     Non-configurable rules:

     * SpaceBetweenTokens
       - Max 1 space between non-space tokens
     * LineBreakAfterSemicolon
       - No one-liners
     * SpaceAroundOperator
       - Every operator (+, -, *, /, =, etc.) must be around spaces
     */

    fun loadConfig(): List<FormatRule> {
        val output = mutableListOf<FormatRule>()
        val optionalRules = readConfig()
        output.addAll(createConfigurableRules(optionalRules))
        output.addAll(createMandatoryRules())
        return listOf()
    }

    private fun createMandatoryRules(): List<FormatRule> {
        val output = mutableListOf<FormatRule>()
        output.add(SpaceAroundOperatorRule())
        output.add(SpaceBetweenTokensRule())
        return output
    }

    private fun createConfigurableRules(rules: Map<String, Any>): List<FormatRule> {
        val output = mutableListOf<FormatRule>()
        for (rule in rules.keys) {
            when (rule) {
                "NoSpaceBeforeColon" -> output.add(NoSpaceBeforeColonRule())
                "NoSpaceAfterColon" -> output.add(NoSpaceAfterColonRule())
                "NoSpaceBeforeEquals" -> output.add(NoSpaceBeforeEqualsRule())
                "NoSpaceAfterEquals" -> output.add(NoSpaceAfterEqualsRule())

                // "lineBreakBeforePrint" -> output.add(LineBreakBeforePrint(rules[rule]))
            }
        }
        return output
    }

    private fun readConfig(): Map<String, Any> {
        val output = mutableMapOf<String, Any>()
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(File(configFile).readText())
        val switchRules: Map<String, Boolean> = data["switch"] as Map<String, Boolean>
        val valueRules: Map<String, Any> = data["setValue"] as Map<String, Any>

        for (rule in switchRules.keys) {
            if (switchRules[rule] == true) {
                output[rule] = true
            }
        }

        for (rule in valueRules.keys) {
            output[rule] = valueRules[rule] as Any
        }

        return output
    }
}
