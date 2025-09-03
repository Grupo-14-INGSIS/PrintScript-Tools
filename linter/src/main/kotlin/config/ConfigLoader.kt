package linter.config

import org.yaml.snakeyaml.Yaml
import java.io.File

class ConfigLoader {
    fun loadYaml(path: String): Map<String, Any> {
        val yaml = Yaml()
        val file = File(path).inputStream()
        val map: Map<String, Any> = yaml.load(file)
        return map
    }
}


// si necesito agregar una regla, se crea su rule (implementacion de la interfaz) su config
/*
// ---CASO DE USO---
val loader = ConfigLoader()
val yamlMap = loader.loadYaml("config.yaml")

val factory = ConfigFactory()
val config = factory.createConfig(yamlMap)

 */
