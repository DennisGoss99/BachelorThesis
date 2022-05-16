import cga.exercise.components.collision.SAP
import cga.exercise.components.collision.TestHitBox
import cga.exercise.components.gravity.GravityObjectContainer
import cga.exercise.components.gravity.GravityProperties
import cga.exercise.components.gravity.IGravity
import cga.exercise.components.gravity.TestGravityObject
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.Test
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.system.measureNanoTime

class GravitySystemTest {



    @Test
    fun checkGravitySystem(){

        val GOC = GravityObjectContainer()

        val testObj1 = TestGravityObject(100f, Vector3f(100f))
        val testObj2 = TestGravityObject(100f, Vector3f(-100f))
        val testObj3 = TestGravityObject(1f, Vector3f(0f))

        GOC.add(testObj1, GravityProperties.source)
        GOC.add(testObj2, GravityProperties.source)
        GOC.add(testObj3, GravityProperties.adopter)

        repeat(1000){GOC.applyGravity()}

        assertEquals(Vector3f(0f), testObj3.getPosition())
    }

    @Test
    fun checkGravitySystemOrbitX(){

        val GOC = GravityObjectContainer()

        val testObj1 = TestGravityObject(2f, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.1f, Vector3f(20f,0f,0f), Vector3f(0f, 0.81694555f,0f))

        GOC.add(testObj1, GravityProperties.source)
        GOC.add(testObj2, GravityProperties.adopter)

        repeat(154){
            GOC.applyGravity()
        }

        assertEquals(20,testObj2.getPosition().x.roundToInt())
    }

    @Test
    fun checkGravitySystemOrbitY(){

        val GOC = GravityObjectContainer()

        val testObj1 = TestGravityObject(6f, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.001f, Vector3f(0f,36f,0f), Vector3f(0f, 0f,1.054672f))

        GOC.add(testObj1, GravityProperties.source)
        GOC.add(testObj2, GravityProperties.adopter)

        repeat(214){
            GOC.applyGravity()
        }
        assertEquals(36, testObj2.getPosition().y.roundToInt())
    }

    @Test
    fun checkGravitySystemOrbitZ(){

        val GOC = GravityObjectContainer()

        val testObj1 = TestGravityObject(80f, Vector3f(0f), Vector3f(0f,0f,0f))
        val testObj2 = TestGravityObject(0.001f, Vector3f(0f,0f,600f), Vector3f(0.9433274f,0f, 0f))

        GOC.add(testObj1, GravityProperties.source)
        GOC.add(testObj2, GravityProperties.adopter)

        repeat(3996){
            GOC.applyGravity()
        }
        assertEquals(600, testObj2.getPosition().z.roundToInt())
    }

    @Test
    fun checkGravitySystemParallel(){
        runBlocking {


            val GOC = GravityObjectContainer()
            val GOCp = GravityObjectContainer()

            val listGOC = mutableListOf<IGravity>()
            val listGOCp = mutableListOf<IGravity>()

            repeat(200){

                val mass = Random.nextInt(1,101).toFloat()
                val pos = Vector3f(Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat())
                val testObj = TestGravityObject(mass, Vector3f(pos), Vector3f(0f,0f,0f))
                val testObj2 = TestGravityObject(mass, Vector3f(pos), Vector3f(0f,0f,0f))

                GOCp.add(testObj, GravityProperties.sourceAndAdopter)
                listGOCp.add(testObj2)

                listGOC.add(testObj)
                GOC.add(testObj2, GravityProperties.sourceAndAdopter)
            }

            var timeSum = 0L
            var timeSumParallel = 0L

            repeat(5000){
                timeSum += measureNanoTime { GOC.applyGravity() }
                timeSumParallel += measureNanoTime { GOCp.applyGravityParallel(Random.nextInt(1,101))}
            }

            for (i in 0 until 100){
                assertEquals(listGOC[i].getPosition(), listGOCp[i].getPosition())
            }

            println("avg sequential : ${(timeSum/5000f)/1000000f}ms")
            println("avg parallel   : ${(timeSumParallel/5000f)/1000000f}ms")
        }
    }

}