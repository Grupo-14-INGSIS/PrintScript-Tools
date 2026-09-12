package formatter.src.main.kotlin

import formatter.src.main.kotlin.formatrule.FormatRule
import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader(
    private val configFile: String,
    private val registry: FormatRuleRegistry = FormatRuleRegistry
) {

    fun loadConfig(version: String = "1.0"): List<FormatRule> {
        val config = readConfig()
        val mandatoryRules = extractMandatoryRulesFromConfig(config)
        val configurableRules = createConfigurableRules(config)
        return (mandatoryRules + configurableRules).toList()
    }

    internal fun extractMandatoryRulesFromConfig(config: Map<String, Any>): List<FormatRule> {
        return config.mapNotNull { (key, value) ->
            registry.createMandatory(key, value)
        }
    }

    internal fun createConfigurableRules(config: Map<String, Any>): List<FormatRule> {
        return config.mapNotNull { (ruleName, ruleValue) ->
            registry.createConfigurable(ruleName, ruleValue)
        }
    }

    internal fun readConfig(): Map<String, Any> {
        val yaml = Yaml()
        val data = yaml.load<Map<String, Any>>(File(configFile).readText())
        return data ?: emptyMap()
    }
}
