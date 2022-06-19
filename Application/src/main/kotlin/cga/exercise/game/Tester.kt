package cga.exercise.game

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class Tester(
    var testResultPath : String = "",
    val cycleSettings : List<Pair<Long, Settings>>,
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