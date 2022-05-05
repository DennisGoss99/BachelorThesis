import cga.exercise.components.collision.SAP
import cga.exercise.components.collision.TestHitBox
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis


class SAPTest {

    val sap = SAP()

    val count = 5000

    fun beforeTest(){
        sap.clear()
        repeat(count){
            val x1 = Random.nextInt(0,count/10).toFloat()
            val x2 = x1 + Random.nextInt(1,5)
            val y1 = Random.nextInt(0,count/10).toFloat()
            val y2 = y1 + Random.nextInt(1,5)
            val z1 = Random.nextInt(0,count/10).toFloat()
            val z2 = z1 + Random.nextInt(1,5)
            sap.insertBox(TestHitBox(sap.idCounter, x1,x2,y1,y2,z1,z2))
        }
        sap.sort()
    }

    @Test
    fun checkCollision(){

        var time = 0f

        repeat(100){
            beforeTest()
            time += measureTimeMillis {sap.checkCollision()}
        }
        println(time/100f )
    }

    @Test
    fun halfAsyncCheckCollision(){
        runBlocking {
            beforeTest()

            var time = 0f

            repeat(100){
                beforeTest()
                time += measureTimeMillis {sap.checkCollisionHalfParallel(it + 1 )}
            }

            println(time/100f )
        }
    }

    @Test
    fun asyncCheckCollision() {
        runBlocking {
            beforeTest()

            var time = 0f

            repeat(100){
                beforeTest()
                time += measureTimeMillis {sap.checkCollisionParallel(it + 1 )}
            }

            println(time/100f )
        }
    }

}