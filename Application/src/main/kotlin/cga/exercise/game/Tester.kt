package cga.exercise.game

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class Tester(
    val cycleCount : Long,
    var testResultPath : String = "",
    val cycleSettings : List<Settings>,
)
{

    companion object{

        private const val testerPath = "testFile.json"

        fun loadTester() : Tester? {
            val testerFile = File(testerPath)

            return if(testerFile.isFile)
            {
                val data = testerFile.bufferedReader().readLine()
                Json.decodeFromString(data)
            }else
                null
        }

        fun saveTester(tester: Tester){
            File(testerPath).writeText(Json.encodeToString(tester))
        }
    }

}