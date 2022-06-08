import cga.exercise.components.properties.gravity.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.Test
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.system.measureNanoTime

class GravitySystemTest {



    @Test
    fun checkGravitySystem(){

        val GOC = GravityManager()

        val testObj1 = TestGravityObject(100f, GravityProperties.source, Vector3f(100f))
        val testObj2 = TestGravityObject(100f, GravityProperties.source, Vector3f(-100f))
        val testObj3 = TestGravityObject(1f, GravityProperties.adopter, Vector3f(0f))

        GOC.add(testObj1)
        GOC.add(testObj2)
        GOC.add(testObj3)

        runBlocking {
            repeat(1000){GOC.applyGravity()}
        }
        assertEquals(Vector3f(0f), testObj3.getPosition())
    }

    @Test
    fun checkGravitySystemOrbitX(){

        val GOC = GravityManager()

        val testObj1 = TestGravityObject(2f, GravityProperties.source, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.1f, GravityProperties.adopter, Vector3f(20f,0f,0f), Vector3f(0f, 0.81694555f,0f))

        GOC.add(testObj1)
        GOC.add(testObj2)
        runBlocking {
            repeat(154) {
                GOC.applyGravity()
            }
        }
        assertEquals(20,testObj2.getPosition().x.roundToInt())
    }

    @Test
    fun checkGravitySystemOrbitY(){

        val GOC = GravityManager()

        val testObj1 = TestGravityObject(6f, GravityProperties.source, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.001f, GravityProperties.adopter, Vector3f(0f,36f,0f), Vector3f(0f, 0f,1.054672f))

        GOC.add(testObj1)
        GOC.add(testObj2)
        runBlocking {
            repeat(214){
                GOC.applyGravity()
            }
        }
        assertEquals(36, testObj2.getPosition().y.roundToInt())
    }

    @Test
    fun checkGravitySystemOrbitZ(){

        val GOC = GravityManager()

        val testObj1 = TestGravityObject(80f, GravityProperties.source, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.001f, GravityProperties.adopter, Vector3f(0f,0f,600f), Vector3f(0.9433274f,0f, 0f))

        GOC.add(testObj1)
        GOC.add(testObj2)
        runBlocking {
            repeat(3996){
                GOC.applyGravity()
            }
        }
        assertEquals(600, testObj2.getPosition().z.roundToInt())
    }

    @Test
    fun checkGravitySystemParallel(){
        runBlocking {


            val GOC = GravityManager()
            val GOCp = ParallelGravityManager(1)

            val listGOC = mutableListOf<IGravity>()
            val listGOCp = mutableListOf<IGravity>()

            repeat(200){

                val mass = Random.nextInt(1,101).toFloat()
                val pos = Vector3f(Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat())
                val testObj = TestGravityObject(mass, GravityProperties.sourceAndAdopter, Vector3f(pos), Vector3f(0f,0f,0f))
                val testObj2 = TestGravityObject(mass, GravityProperties.sourceAndAdopter, Vector3f(pos), Vector3f(0f,0f,0f))

                GOCp.add(testObj)
                listGOCp.add(testObj2)

                listGOC.add(testObj)
                GOC.add(testObj2)
            }

            var timeSum = 0L
            var timeSumParallel = 0L
            runBlocking {
                repeat(5000) {
                    timeSum += measureNanoTime { GOC.applyGravity() }
                    GOCp.jobCount = Random.nextInt(1, 101)
                    timeSumParallel += measureNanoTime { GOCp.applyGravity() }
                }
            }

            for (i in 0 until 100){
                assertEquals(listGOC[i].getPosition(), listGOCp[i].getPosition())
            }

            println("avg sequential : ${(timeSum/5000f)/1000000f}ms")
            println("avg parallel   : ${(timeSumParallel/5000f)/1000000f}ms")
        }
    }

}