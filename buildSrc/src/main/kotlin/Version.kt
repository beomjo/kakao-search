import java.io.FileInputStream
import java.util.*

object Version {
    private const val VERSION_FILE_NAME = "version.properties"

    data class VersionProperty(
        val code: Int,
        val name: String
    )

    fun getVersionProperty(): VersionProperty {
        val prop = loadVersionVersionPropertyFile()
        return VersionProperty(
            prop.getProperty("version.code", "1").toInt(),
            prop.getProperty("version.name", "1")
        )
    }

    private fun loadVersionVersionPropertyFile(): Properties {
        FileInputStream(VERSION_FILE_NAME).use { return Properties().apply { load(it) } }
    }
}