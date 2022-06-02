package cga.exercise.game

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class Settings(
    var executeParallel : Boolean = true,
    var jobCount : Int = Runtime.getRuntime().availableProcessors().coerceAtLeast(2),
    var updateFrequency : Int = 60,
    var objectCount : Int = 0,
    var useSampleData : Boolean = true,
    var shouldTestResultOutput : Boolean = true
    )
{
    companion object{

        private const val settingsPath = "settings.json"

        fun loadSettings() : Settings {
            val settingsFile = File(settingsPath)

            return if(settingsFile.isFile)
            {
                val data = settingsFile.bufferedReader().readLine()
                Json.decodeFromString(data)
            }else
                Settings()
        }

        fun saveSettings(settings: Settings){
            File(settingsPath).writeText(Json.encodeToString(settings))
        }
    }
}