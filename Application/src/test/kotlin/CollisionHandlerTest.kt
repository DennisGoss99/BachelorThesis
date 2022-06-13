import cga.exercise.components.properties.applier.CollisionHandler
import cga.exercise.components.properties.applier.ParallelCollisionHandler
import cga.exercise.components.properties.applier.TestApplierObject
import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.collision.ParallelSAP
import cga.exercise.components.properties.collision.SAP
import cga.exercise.components.properties.collision.TestHitBox
import cga.exercise.components.properties.gravity.GravityHitBox
import cga.exercise.components.properties.gravity.GravityManager
import cga.exercise.components.properties.gravity.GravityProperties
import cga.framework.printlnTimeMillis
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.Test
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import kotlin.system.measureTimeMillis

class CollisionHandlerTest {

    companion object{
        private val hitBoxCount = 10000
        private val repeat = 100
    }

    @Test
    fun checkCollisionHandelParallel() = runBlocking {

        val sap = SAP()
        val collisionHandler = CollisionHandler()
        val sap2 = SAP()
        val collisionHandler2 = ParallelCollisionHandler(4)

        collisionHandler.impactScatterValue = 99f
        collisionHandler2.impactScatterValue = 99f

        var move = 0f
        repeat(100000){
            if(it % 2 == 0)
                move += 10f

            val position = Vector3f((Random.nextFloat() -0.5f) * 4f + move,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
            val velocity = Vector3f((Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
            val mass = Random.nextFloat() * 10

            with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity), GravityProperties.nothing, true)){
                sap.insertBox(this)
                collisionHandler.add(this)
            }
            with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity), GravityProperties.nothing, true)){
                sap2.insertBox(this)
                collisionHandler2.add(this)
            }
        }

        repeat(5){
            sap.sort()
            sap.checkCollision()
            //collisionHandler.handleCollision()
            printlnTimeMillis("s") {collisionHandler.handleCollision()}
        }
        repeat(5){
            sap2.sort()
            sap2.checkCollision()
            //collisionHandler2.handleCollision()
            printlnTimeMillis("p") {collisionHandler2.handleCollision()}
        }

        collisionHandler.hitBoxes.forEachIndexed{ index, hitBox ->
            val hitBox2 = collisionHandler2.hitBoxes[index]
            assertEquals("hitBox[$index] PosX", hitBox.getPosition().x, hitBox2.getPosition().x, 0.0001f)
            assertEquals("hitBox[$index] PosY", hitBox.getPosition().y, hitBox2.getPosition().y, 0.0001f)
            assertEquals("hitBox[$index] PosZ", hitBox.getPosition().z, hitBox2.getPosition().z, 0.0001f)
            assertEquals("hitBox[$index] velocityX", hitBox.velocity.x, hitBox2.velocity.x, 0.0001f)
            assertEquals("hitBox[$index] velocityY", hitBox.velocity.y, hitBox2.velocity.y, 0.0001f)
            assertEquals("hitBox[$index] velocityZ", hitBox.velocity.z, hitBox2.velocity.z, 0.0001f)
        }
    }

    private val removeHitBox : ((Int) -> Unit) = { id -> sap.remove(id); collisionHandler.remove(id) }
    private val addHitBox : ((mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f) -> Unit) = { mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f -> with(GravityHitBox(IHitBox.idCounter, mass, GravityProperties.nothing, pos, scale, velocity)){ sap.insertBox(this); collisionHandler.add(this) } }

    private val sap = SAP()
    private val collisionHandler = CollisionHandler(removeHitBox, addHitBox)

    private val removeHitBox2 : ((Int) -> Unit) = { id -> sap2.remove(id); collisionHandler2.remove(id) }
    private val addHitBox2 : ((mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f) -> Unit) = { mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f -> with(GravityHitBox(IHitBox.idCounter, mass, GravityProperties.nothing, pos, scale, velocity)){ sap2.insertBox(this); collisionHandler2.add(this) } }

    private val sap2 = SAP()
    private val collisionHandler2 = ParallelCollisionHandler(4, removeHitBox2, addHitBox2)


    @Test
    fun checkScatterParallel() = runBlocking {

        collisionHandler.impactScatterValue = 0.1f
        collisionHandler.scatterAmount = 20

        collisionHandler2.impactScatterValue = 0.1f
        collisionHandler2.scatterAmount = 20

        var move = -1000f
        repeat(2000){
            if(it % 2 == 0)
                move += 200f

            val position = Vector3f(move,(Random.nextFloat() -0.5f) ,(Random.nextFloat() -0.5f))
            val velocity = Vector3f((Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
            val mass = Random.nextFloat() * 10

            with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity).mul(if(it % 2 == 0) 1f else -1f), GravityProperties.nothing, true)){
                sap.insertBox(this)
                collisionHandler.add(this)
            }
            with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity).mul(if(it % 2 == 0) 1f else -1f), GravityProperties.nothing, true)){
                sap2.insertBox(this)
                collisionHandler2.add(this)
            }
        }


        sap.sort()
        sap.checkCollision()
        //collisionHandler.handleCollision()
        printlnTimeMillis("s") {collisionHandler.handleCollision()}


        sap2.sort()
        sap2.checkCollision()
        //collisionHandler2.handleCollision()
        printlnTimeMillis("p") {collisionHandler2.handleCollision()}


        collisionHandler.hitBoxes.forEachIndexed{ index, hitBox ->

            val h2 = collisionHandler2.hitBoxes.firstOrNull() {
                it !== hitBox && hitBox.getPosition().x == it.getPosition().x && hitBox.getPosition().y == it.getPosition().y && hitBox.getPosition().z == it.getPosition().z
            }

            assertEquals("hitBox[$index] PosX", hitBox.getPosition().x, h2?.getPosition()?.x ?: -99999999f, 0.0001f)
            assertEquals("hitBox[$index] PosY", hitBox.getPosition().y, h2?.getPosition()?.y ?: -99999999f, 0.0001f)
            assertEquals("hitBox[$index] PosZ", hitBox.getPosition().z, h2?.getPosition()?.z ?: -99999999f, 0.0001f)
            assertEquals("hitBox[$index] velocityX", hitBox.velocity.x, h2?.velocity?.x ?: -99999999f, 0.0001f)
            assertEquals("hitBox[$index] velocityY", hitBox.velocity.y, h2?.velocity?.y ?: -99999999f, 0.0001f)
            assertEquals("hitBox[$index] velocityZ", hitBox.velocity.z, h2?.velocity?.z ?: -99999999f, 0.0001f)
        }
    }

    @Test
    fun collisionHandelSpeedTest() = runBlocking {

        val time = measureTimeMillis {
            repeat(repeat){
                val sap = SAP()
                val collisionHandler = CollisionHandler()

                var move = 0f
                repeat(hitBoxCount){
                    if(it % 2 == 0)
                        move += 10f

                    val position = Vector3f((Random.nextFloat() -0.5f) * 4f + move,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
                    val velocity = Vector3f((Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
                    val mass = Random.nextFloat() * 10

                    with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity), GravityProperties.nothing, true)){
                        sap.insertBox(this)
                        collisionHandler.add(this)
                    }
                }


                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()

                sap.clear()
                collisionHandler.clear()
            }
        }

        println("avg time: ${"%.2f".format(time.toFloat()/repeat)}ms")
    }

    @Test
    fun parallelCollisionHandelSpeedTest() = runBlocking {

        val time = measureTimeMillis {
            repeat(repeat){
                val sap2 = SAP()
                val collisionHandler2 = ParallelCollisionHandler(4)

                var move = 0f
                repeat(hitBoxCount){
                    if(it % 2 == 0)
                        move += 10f

                    val position = Vector3f((Random.nextFloat() -0.5f) * 4f + move,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
                    val velocity = Vector3f((Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f ,(Random.nextFloat() -0.5f) * 4f )
                    val mass = Random.nextFloat() * 10

                    with(TestApplierObject(IHitBox.idCounter, mass, Vector3f(position), Vector3f(1f), Vector3f(velocity), GravityProperties.nothing, true)){
                        sap2.insertBox(this)
                        collisionHandler2.add(this)
                    }
                }


                sap2.sort()
                sap2.checkCollision()
                collisionHandler2.handleCollision()

                sap2.clear()
                collisionHandler2.clear()
            }
        }

        println("avg time: ${"%.2f".format(time.toFloat()/repeat)}ms")
    }

}