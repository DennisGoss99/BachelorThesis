import cga.exercise.components.collision.SAP
import cga.exercise.components.collision.TestHitBox
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.BeforeClass
import org.junit.Test
import kotlin.random.Random
import kotlin.system.measureTimeMillis


class SAPTest {



    @Test
    fun checkSort(){

        val sap = SAP()
        val sap2 = SAP()

        repeat(1000){
            val testBox = TestHitBox(sap.idCounter, Vector3f(Random.nextInt(-1000,1000).toFloat(),Random.nextInt(-1000,1000).toFloat(),Random.nextInt(-1000,1000).toFloat()))
            sap.insertBox(testBox)
            sap2.insertBox(testBox)
        }

        sap.sort()
        sap2.sortParallel()

        for(i in 0 until sap.idCounter)
            assertEquals(sap.hitBoxes[i], sap2.hitBoxes[i])
    }

    @Test
    fun checkCollision(){
        val sap = SAP()

        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(0f,-5.40142f,0f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(1.19985f,-2.96523f,-1.18883f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-1.36591f,-1.35558f,0.969402f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(0.020946f,-0.334908f,-0.316789f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.971955f,-4.24241f,2.78168f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(3.40919f,1.00089f,0.245847f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.306667f,-0.456765f,4.26992f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(0.491334f,-3.66383f,1.29069f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(1.80999f,1.32266f,2.5045f)))
        sap.sort()
        sap.checkCollision()

        assertEquals(sap.collisionCount(),5)

        assertEquals(sap.hitBoxes[0].collidedWith.map { it.id }.sorted(), listOf<Int>(7))
        assertEquals(sap.hitBoxes[1].collidedWith.map { it.id }.sorted(), listOf<Int>())
        assertEquals(sap.hitBoxes[2].collidedWith.map { it.id }.sorted(), listOf<Int>(3))
        assertEquals(sap.hitBoxes[3].collidedWith.map { it.id }.sorted(), listOf<Int>(2))
        assertEquals(sap.hitBoxes[4].collidedWith.map { it.id }.sorted(), listOf<Int>(7))
        assertEquals(sap.hitBoxes[5].collidedWith.map { it.id }.sorted(), listOf<Int>())
        assertEquals(sap.hitBoxes[6].collidedWith.map { it.id }.sorted(), listOf<Int>())
        assertEquals(sap.hitBoxes[7].collidedWith.map { it.id }.sorted(), listOf<Int>(0,4))
        assertEquals(sap.hitBoxes[8].collidedWith.map { it.id }.sorted(), listOf<Int>())
    }

    @Test
    fun checkCollision2(){
        val sap = SAP()

        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-1.81922f,0.13185f,1.43538f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(0f,2f,0f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.570769f,-0.697269f,0.466469f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-2.56339f,1.2581f,2.37958f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-1.30461f,2.89979f,1.12968f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-2.7742f,-1.23996f,1.75068f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.848593f,-3.80494f,1.73219f)))
        sap.sort()
        sap.checkCollision()

        assertEquals(sap.collisionCount(),6)

        assertEquals(sap.hitBoxes[0].collidedWith.map { it.id }.sorted(), listOf<Int>(1, 2, 3, 5))
        assertEquals(sap.hitBoxes[1].collidedWith.map { it.id }.sorted(), listOf<Int>(0, 4))
        assertEquals(sap.hitBoxes[2].collidedWith.map { it.id }.sorted(), listOf<Int>(0, 3))
        assertEquals(sap.hitBoxes[3].collidedWith.map { it.id }.sorted(), listOf<Int>(0, 2, 4))
        assertEquals(sap.hitBoxes[4].collidedWith.map { it.id }.sorted(), listOf<Int>(1, 3))
        assertEquals(sap.hitBoxes[5].collidedWith.map { it.id }.sorted(), listOf<Int>(0))
        assertEquals(sap.hitBoxes[6].collidedWith.map { it.id }.sorted(), listOf<Int>())
    }

    @Test
    fun checkCollision3(){
        val sap = SAP()

        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-5.82668f,-0.597045f,-5.71673f), 6f))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.570769f,-0.697269f,0.635953f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.505851f,2.89979f,0.392071f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-2.14847f,1.11866f,2.23375f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-2.7742f,-1.23996f,3.70352f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-4.5489f,0.114777f,5.35729f)))
        sap.insertBox(TestHitBox(sap.idCounter, Vector3f(-0.848593f,-3.80494f,1.32927f)))
        sap.sort()
        sap.checkCollision()

        assertEquals(sap.collisionCount(),6)

        assertEquals(sap.hitBoxes[0].collidedWith.map { it.id }.sorted(), listOf<Int>(1, 2))
        assertEquals(sap.hitBoxes[1].collidedWith.map { it.id }.sorted(), listOf<Int>(0, 3))
        assertEquals(sap.hitBoxes[2].collidedWith.map { it.id }.sorted(), listOf<Int>(0, 3))
        assertEquals(sap.hitBoxes[3].collidedWith.map { it.id }.sorted(), listOf<Int>(1, 2))
        assertEquals(sap.hitBoxes[4].collidedWith.map { it.id }.sorted(), listOf<Int>(5))
        assertEquals(sap.hitBoxes[5].collidedWith.map { it.id }.sorted(), listOf<Int>(4))
        assertEquals(sap.hitBoxes[6].collidedWith.map { it.id }.sorted(), listOf<Int>())
    }

}