import java.io.FileInputStream
import java.util.*

object Version {
    private const val VERSION_FILE_NAME = "version.properties"

    data class Property(
        val code: Int,
        val name: String
    )

    fun getVersionProperty(): Property {
        val prop = loadVersionVersionPropertyFile()
        return Property(
            prop.getProperty("version.code", "1").toInt(),
            prop.getProperty("version.name", "1")
        )
    }

    private fun loadVersionVersionPropertyFile(): Properties {
        FileInputStream(VERSION_FILE_NAME).use { return Properties().apply { load(it) } }
    }
}