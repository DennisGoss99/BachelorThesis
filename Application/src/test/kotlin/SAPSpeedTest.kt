import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.collision.ParallelSAP
import cga.exercise.components.properties.collision.SAP
import cga.exercise.components.properties.collision.TestHitBox
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.BeforeClass
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SAPSpeedTest {


    companion object{
        val sap = SAP()
        val sap2 = ParallelSAP(1)

        private val hitBoxCount = 5000
        private val jobCount = 100

        @BeforeClass
        @JvmStatic
        fun setup(){
            runBlocking {
                sap.clear()
                sap2.clear()
                sap2.jobCount = jobCount

                repeat(hitBoxCount){
                    val x1 = Random.nextInt(0,501).toFloat()
                    val x2 = x1 + Random.nextInt(1,5)
                    val y1 = Random.nextInt(0,501).toFloat()
                    val y2 = y1 + Random.nextInt(1,5)
                    val z1 = Random.nextInt(0,501).toFloat()
                    val z2 = z1 + Random.nextInt(1,5)
                    sap.insertBox(TestHitBox(IHitBox.idCounter, x1,x2,y1,y2,z1,z2))
                    sap2.insertBox(TestHitBox(IHitBox.idCounter, x1,x2,y1,y2,z1,z2))
                }

                sap.sort()
                sap2.sort()
            }
        }
    }


    @Test
    fun checkCollision(){

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
                val tempTime = measureTimeMillis { sap.checkCollision()}
                timeSum += tempTime

                if (bestJobTime > tempTime){
                    bestJobTime = tempTime
                    bestJobCount = it
                }

                Assert.assertEquals(count, sap.collisionCount())
            }

            println("best[$bestJobCount]:\t${bestJobTime} ms")
            println("avg:\t\t${timeSum.toFloat()/ jobCount} ms")
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
                sap2.jobCount = it + 1
                val tempTime = measureTimeMillis { sap2.checkCollision()}
                timeSum += tempTime

                if (bestJobTime > tempTime){
                    bestJobTime = tempTime
                    bestJobCount = it
                }

                Assert.assertEquals(count, sap2.collisionCount())
            }

            println("best[$bestJobCount]:\t${bestJobTime} ms")
            println("avg:\t\t${timeSum.toFloat()/ jobCount} ms")
        }
    }

}