import cga.exercise.components.collision.SAP
import cga.exercise.components.collision.TestHitBox
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis


class SAPTest {

    companion object{
        val sap = SAP()

        private val hitBoxCount = 5000
        private val jobCount = 100

        @BeforeClass @JvmStatic
        fun setup(){
            sap.clear()
            repeat(hitBoxCount){
                val x1 = Random.nextInt(0,hitBoxCount/10).toFloat()
                val x2 = x1 + Random.nextInt(1,5)
                val y1 = Random.nextInt(0,hitBoxCount/10).toFloat()
                val y2 = y1 + Random.nextInt(1,5)
                val z1 = Random.nextInt(0,hitBoxCount/10).toFloat()
                val z2 = z1 + Random.nextInt(1,5)
                sap.insertBox(TestHitBox(sap.idCounter, x1,x2,y1,y2,z1,z2))
            }
            sap.sort()
        }
    }


    @Test
    fun checkCollision(){

        sap.checkCollision()
        val count = sap.collisionCount()

        println("JobCount  : [$jobCount]")
        println("BoxCount  : [$hitBoxCount]")
        println("Collisions: [$count]")
        println("-------------------------------")

        var timeSum= 0L
        var bestJobCount = 0
        var bestJobTime = Long.MAX_VALUE

        repeat(jobCount){
            val tempTime = measureTimeMillis {sap.checkCollision()}
            timeSum += tempTime

            if (bestJobTime > tempTime){
                bestJobTime = tempTime
                bestJobCount = it
            }

            assertEquals(count, sap.collisionCount())
        }

        println("best[$bestJobCount]:\t${bestJobTime} ms")
        println("avg:\t\t${timeSum.toFloat()/jobCount} ms")
    }

    @Test
    fun halfAsyncCheckCollision(){
        runBlocking {

            sap.checkCollision()
            val count = sap.collisionCount()

            println("JobCount  : [$jobCount]")
            println("BoxCount  : [$hitBoxCount]")
            println("Collisions: [$count]")
            println("-------------------------------")

            var timeSum= 0L
            var bestJobCount = 0
            var bestJobTime = Long.MAX_VALUE

            repeat(jobCount){
                val tempTime = measureTimeMillis {sap.checkCollisionHalfParallel(it + 1)}
                timeSum += tempTime

                if (bestJobTime > tempTime){
                    bestJobTime = tempTime
                    bestJobCount = it
                }

                assertEquals(count, sap.collisionCount())
            }

            println("best[$bestJobCount]:\t${bestJobTime} ms")
            println("avg:\t\t${timeSum.toFloat()/jobCount} ms")
        }
    }

    @Test
    fun asyncCheckCollision() {
        runBlocking {

            sap.checkCollision()
            val count = sap.collisionCount()

            println("JobCount  : [$jobCount]")
            println("BoxCount  : [$hitBoxCount]")
            println("Collisions: [$count]")
            println("-------------------------------")

            var timeSum= 0L
            var bestJobCount = 0
            var bestJobTime = Long.MAX_VALUE

            repeat(jobCount){
                val tempTime = measureTimeMillis {sap.checkCollisionParallel(it + 1)}
                timeSum += tempTime

                if (bestJobTime > tempTime){
                    bestJobTime = tempTime
                    bestJobCount = it
                }

                assertEquals(count, sap.collisionCount())
            }

            println("best[$bestJobCount]:\t${bestJobTime} ms")
            println("avg:\t\t${timeSum.toFloat()/jobCount} ms")
        }
    }

}