import cga.exercise.components.properties.applier.AbstractCollisionHandler
import cga.exercise.components.properties.applier.CollisionHandler
import cga.exercise.components.properties.applier.TestApplierObject
import cga.exercise.components.properties.collision.SAP
import cga.exercise.components.properties.gravity.GravityManager
import cga.exercise.components.properties.gravity.GravityProperties
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.Test
import junit.framework.Assert.assertEquals

class ObjectCollisionTest {

    @Test
    fun simpleMoveBoxOutOfRadiusX(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, 0f, 0f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(-2f, 0f, 0f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0f), Vector3f(2f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(-3f, 0f, 0f))

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0f), Vector3f(1f), Vector3f(-5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(2f, 0f, 0f))

        val testOb12 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0f), Vector3f(1f), Vector3f(-5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb12) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb12", testOb12, Vector3f(2f, 0f, 0f))

        val testOb13 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0f), Vector3f(2f), Vector3f(-5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb13) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb13", testOb13, Vector3f(3f, 0f, 0f))

        val testOb21 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, -2f, 2f), Vector3f(2f), Vector3f(-5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb21) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb21", testOb21, Vector3f(3f, -2f, 2f))

        val testOb22 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(.5f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb22) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb22", testOb22, Vector3f(-1.5f, 0f, 0f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesX(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals("$testMessage x", finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals("$testMessage y", finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals("$testMessage z", finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, 0f, 0f))

        testOb1.velocity = Vector3f(-5f,0f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(-1.5f, 0f, 0f))
        assertEquals("testOb02 testOb1", testOb1.pos, Vector3f(0.5f, 0f, 0f))

        testOb1.velocity = Vector3f(-5f,0f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(2f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(-2.5f, 0f, 0f))
        assertEquals("testOb3 testOb1", testOb1.pos, Vector3f(1.5f, 0f, 0f))

        testOb1.velocity = Vector3f(5f,0f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(2f), Vector3f(-5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(1.5f, 0f, 0f))
        assertEquals("testOb04 testOb1", testOb1.pos, Vector3f(-2.5f, 0f, 0f))

        testOb1.velocity = Vector3f(-2.5f, 0f, 0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(-5f/3f, 0f, 0f))
        assertEquals("testOb11 testOb1", testOb1.pos.x, Vector3f(1f/3f, 0f, 0f).x,0.001f)

        testOb1.velocity = Vector3f(-5f,0f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb22 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, 0f), Vector3f(.5f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb22) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb22", testOb22, Vector3f(-1.25f, 0f, 0f))
        assertEquals("testOb22 testOb1", testOb1.pos, Vector3f(0.25f, 0f, 0f))

        testOb1.velocity = Vector3f(3f, 0f, 0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(6f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(-1f -1f/3f, 0f, 0f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(2f/3f, 0f, 0f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun simpleMoveBoxOutOfRadiusY(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,-1f,0f), Vector3f(1f), Vector3f(0f,5f,0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(0f,-2f,0f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,1f,0f), Vector3f(1f), Vector3f(0f,5f,0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f,-2f,0f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,1f,0f), Vector3f(2f), Vector3f(0f,5f,0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(0f,-3f, 0f))

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,1f,0f), Vector3f(1f), Vector3f(0f,-5f,0f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(0f,2f,  0f))

        val testOb12 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,1f,0f), Vector3f(1f), Vector3f(0f,-5f,0f), GravityProperties.nothing, true)
        with(testOb12) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb12", testOb12, Vector3f(0f,2f,  0f))

        val testOb13 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,1f,0f), Vector3f(2f), Vector3f(0f,-5f,0f), GravityProperties.nothing, true)
        with(testOb13) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb13", testOb13, Vector3f(0f,3f,0f))

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f( -2f, 1f,2f), Vector3f(2f), Vector3f( 0f,-5f, 0f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(  -2f,3f,2f))

    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesY(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals(testMessage, finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals(testMessage, finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(1f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(0f, -2f, 0f))

        testOb1.velocity = Vector3f(0f,-5f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(1f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f, -1.5f, 0f))
        assertEquals("testOb02 testOb1", testOb1.pos, Vector3f(0f, 0.5f, 0f))

        testOb1.velocity = Vector3f(0f,-5f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(2f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(0f, -2.5f, 0f))
        assertEquals("testOb3 testOb1", testOb1.pos, Vector3f(0f, 1.5f, 0f))

        testOb1.velocity = Vector3f(0f,5f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(2f), Vector3f(0f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(0f, 1.5f, 0f))
        assertEquals("testOb04 testOb1", testOb1.pos, Vector3f(0f, -2.5f, 0f))

        testOb1.velocity = Vector3f(0f, -2.5f, 0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(1f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(0f, -5f/3f, 0f))
        assertEquals("testOb11 testOb1", testOb1.pos.x, Vector3f(0f, 1f/3f, 0f).x,0.001f)

        testOb1.velocity = Vector3f(0f,-5f,0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb22 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, 0f), Vector3f(.5f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb22) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb22", testOb22, Vector3f(0f, -1.25f, 0f))
        assertEquals("testOb22 testOb1", testOb1.pos, Vector3f(0f, 0.25f, 0f))

        testOb1.velocity = Vector3f(0f, 3f, 0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 6f, 0f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(0f, -1f -1f/3f, 0f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(0f, 2f/3f, 0f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun simpleMoveBoxOutOfRadiusZ(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,-1f), Vector3f(1f), Vector3f(0f,0f,5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(0f,0f,-2f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,1f), Vector3f(1f), Vector3f(0f,0f,5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f,0f,-2f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,1f), Vector3f(2f), Vector3f(0f,0f,5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(0f, 0f,-3f))

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,1f), Vector3f(1f), Vector3f(0f,0f,-5f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(0f,0f,2f))

        val testOb12 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,1f), Vector3f(1f), Vector3f(0f,0f,-5f), GravityProperties.nothing, true)
        with(testOb12) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb12", testOb12, Vector3f(0f,0f,2f))

        val testOb13 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,1f), Vector3f(2f), Vector3f(0f,0f,-5f), GravityProperties.nothing, true)
        with(testOb13) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb13", testOb13, Vector3f(0f,0f,3f))

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(-2f, 2f, 1f), Vector3f(2f), Vector3f(0f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(-2f,2f, 3f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesZ(){
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage : String, testApplierObject : TestApplierObject, finalPos : Vector3f){
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals(testMessage, finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals(testMessage, finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(1f), Vector3f(0f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(0f, 0f, -2f))

        testOb1.velocity = Vector3f(0f,0f,-5f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(1f), Vector3f(0f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f, 0f, -1.5f))
        assertEquals("testOb02 testOb1", testOb1.pos, Vector3f(0f, 0f, 0.5f))

        testOb1.velocity = Vector3f(0f,0f,-5f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(2f), Vector3f(0f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb3", testOb03, Vector3f(0f, 0f, -2.5f))
        assertEquals("testOb3 testOb1", testOb1.pos, Vector3f(0f, 0f, 1.5f))

        testOb1.velocity = Vector3f(0f,0f,5f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(2f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(2f), Vector3f(0f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(0f, 0f, 1.5f))
        assertEquals("testOb04 testOb1", testOb1.pos, Vector3f(0f, 0f, -2.5f))

        testOb1.velocity = Vector3f(0f, 0f, -2.5f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb11 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(1f), Vector3f(0f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb11) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb11", testOb11, Vector3f(0f, 0f, -5f/3f))
        assertEquals("testOb11 testOb1", testOb1.pos.x, Vector3f(0f, 0f, 1f/3f).x,0.001f)

        testOb1.velocity = Vector3f(0f,0f,-5f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb22 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, -1f), Vector3f(.5f), Vector3f(0f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb22) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb22", testOb22, Vector3f(0f, 0f, -1.25f))
        assertEquals("testOb22 testOb1", testOb1.pos, Vector3f(0f, 0f, 0.25f))

        testOb1.velocity = Vector3f(0f, 0f, 3f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 6f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(0f, 0f, -1f -1f/3f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(0f, 0f, 2f/3f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun simpleMoveBoxOutOfRadiusXY() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(1f), Vector3f(5f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, -2f, 0f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(1f), Vector3f(-5f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(2f, 2f, 0f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 0f), Vector3f(1f), Vector3f(5f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(-2f, -2f, 0f))

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 0f), Vector3f(1f), Vector3f(-5f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(2f, 2f, 0f))

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0.5f, 0f), Vector3f(0.5f), Vector3f(-5f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(1.5f, 1f, 0f))

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0.5f, 0f), Vector3f(0.5f), Vector3f(-5f, -2.5f, 0f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(1.5f, 0.75f, 0f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesXY() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals("$testMessage x", finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals("$testMessage y", finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals("$testMessage z", finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(1f), Vector3f(5f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, -2f, 0f))
        assertEquals("testOb01 testOb1", testOb1.pos, Vector3f(0f, 0f, 0f))

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(1f,1f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(1f), Vector3f(-5f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(1.5f, 1.5f, 0f))
        assertEquals("testOb02 testOb1", Vector3f(-0.5f, -0.5f, 0f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(5f,5f,0f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(2f), Vector3f(-5f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(1.5f, 1.5f, 0f))
        assertEquals("testOb03 testOb1", Vector3f(-2.5f, -2.5f, 0f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(-2.5f,-2.5f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, 0f), Vector3f(1f), Vector3f(5f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(-5f/3f, -5f/3f, 0f))
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.x,0.001f)
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.y,0.001f)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,5f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(-2f, 0f, 0f))
        assertEquals("testOb05 testOb1", Vector3f(0f, -2f, 0f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(-5f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 0f), Vector3f(1f), Vector3f(0f, -5f, 0f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(1f, 2f, 0f))
        assertEquals("testOb06 testOb1", Vector3f(3f, 0f, 0f), testOb1.pos)

        testOb1.velocity = Vector3f(3f, 3f, 0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(6f, 6f, 0f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(-1f -1f/3f, -1f -1f/3f, 0f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(2f/3f, 2f/3f, 0f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun moveBoxOutOfRadiusXZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(1f), Vector3f(5f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, 0f, -2f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(1f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(2f, 0f, 2f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 1f), Vector3f(1f), Vector3f(5f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(-2f, 0f, -2f))

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 1f), Vector3f(1f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(2f, 0f, 2f))

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0.5f), Vector3f(0.5f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(1.5f, 0f, 1f))

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 0.5f), Vector3f(0.5f), Vector3f(-5f, 0f, -2.5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(1.5f, 0f, 0.75f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesXZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals("$testMessage x", finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals("$testMessage y", finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals("$testMessage z", finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(1f), Vector3f(5f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, 0f, -2f))
        assertEquals("testOb01 testOb1", testOb1.pos, Vector3f(0f, 0f, 0f))

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(1f,0f,1f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(1f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(1.5f, 0f, 1.5f))
        assertEquals("testOb02 testOb1", Vector3f(-0.5f, 0f, -0.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(5f,0f,5f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(2f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(1.5f, 0f, 1.5f))
        assertEquals("testOb03 testOb1", Vector3f(-2.5f, 0f, -2.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(-2.5f,0f,-2.5f)
        testOb1.scale = Vector3f(1f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, 0f, -1f), Vector3f(1f), Vector3f(5f, 0f, 5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(-5f/3f, 0f, -5f/3f))
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.x,0.001f)
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.z,0.001f)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,5f)
        testOb1.scale = Vector3f(1f)

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(5f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(-2f, 0f, 0f))
        assertEquals("testOb05 testOb1", Vector3f(0f, 0f, -2f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(-5f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 0f, 1f), Vector3f(1f), Vector3f(0f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(1f, 0f, 2f))
        assertEquals("testOb06 testOb1", Vector3f(3f, 0f, 0f), testOb1.pos)

        testOb1.velocity = Vector3f(3f, 0f, 3f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(6f, 0f, 6f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(-1f -1f/3f, 0f, -1f -1f/3f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(2f/3f, 0f, 2f/3f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun moveBoxOutOfRadiusYZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f( 0f, -1f,-1f), Vector3f(1f), Vector3f( 0f, 5f,5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f( 0f, -2f,-2f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f( 0f, -1f,-1f), Vector3f(1f), Vector3f( 0f, -5f,-5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f, 2f, 2f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1f, 1f), Vector3f(1f), Vector3f(0f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(0f, -2f, -2f))

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1f, 1f), Vector3f(1f), Vector3f(0f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(0f, 2f, 2f))

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1f, 0.5f), Vector3f(0.5f), Vector3f(0f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(0f, 1.5f, 1f))

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1f, 0.5f), Vector3f(0.5f), Vector3f(0f, -5f, -2.5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(0f, 1.5f, 0.75f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesYZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals("$testMessage x", finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals("$testMessage y", finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals("$testMessage z", finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, -1f), Vector3f(1f), Vector3f(0f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(0f, -2f, -2f))
        assertEquals("testOb01 testOb1", testOb1.pos, Vector3f(0f, 0f, 0f))

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,1f,1f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, -1f), Vector3f(1f), Vector3f(0f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(0f, 1.5f, 1.5f))
        assertEquals("testOb02 testOb1", Vector3f(0f, -0.5f, -0.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,5f,5f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, -1f), Vector3f(2f), Vector3f(0f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(0f, 1.5f, 1.5f))
        assertEquals("testOb03 testOb1", Vector3f(0f, -2.5f, -2.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,-2.5f,-2.5f)
        testOb1.scale = Vector3f(1f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, -1f, -1f), Vector3f(1f), Vector3f(0f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(0f, -5f/3f, -5f/3f))
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.y,0.001f)
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.z,0.001f)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,5f)
        testOb1.scale = Vector3f(1f)

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(0f, -2f, 0f))
        assertEquals("testOb05 testOb1", Vector3f(0f, 0f, -2f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,-5f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1f, 1f), Vector3f(1f), Vector3f(0f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(0f, 1f, 2f))
        assertEquals("testOb06 testOb1", Vector3f(0f, 3f, 0f), testOb1.pos)

        testOb1.velocity = Vector3f(0f, 3f, 3f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 6f, 6f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(0f, -1f -1f/3f, -1f -1f/3f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(0f, 2f/3f, 2f/3f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun moveBoxOutOfRadiusXYZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals(testMessage, finalPos, testApplierObject.pos)
            }
        }

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f( -1f, -1f,-1f), Vector3f(1f), Vector3f( 5f, 5f,5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f( -2f, -2f,-2f))

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f( -1f, -1f,-1f), Vector3f(1f), Vector3f( -5f, -5f,-5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(2f, 2f, 2f))

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 1f), Vector3f(1f), Vector3f(5f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(-2f, -2f, -2f))

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 1f), Vector3f(1f), Vector3f(-5f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(2f, 2f, 2f))

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0.5f, 1f, 0.5f), Vector3f(0.5f), Vector3f(-5f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(1f, 1.5f, 1f))

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(0.5f, 1f, 0.5f), Vector3f(0.5f), Vector3f(-2.5f, -5f, -2.5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(0.75f, 1.5f, 0.75f))
    }

    @Test
    fun moveBoxOutOfRadiusBothBoxesXYZ() {
        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        fun testApplierObject(testMessage: String, testApplierObject: TestApplierObject, finalPos: Vector3f) {
            runBlocking {
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()

                sap.remove(testApplierObject)
                abstractCollisionHandler.remove(testApplierObject)

                assertEquals("$testMessage x", finalPos.x, testApplierObject.pos.x, 0.001f)
                assertEquals("$testMessage y", finalPos.y, testApplierObject.pos.y, 0.001f)
                assertEquals("$testMessage z", finalPos.z, testApplierObject.pos.z, 0.001f)
            }
        }

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb01 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, -1f), Vector3f(1f), Vector3f(5f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb01) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb01", testOb01, Vector3f(-2f, -2f, -2f))
        assertEquals("testOb01 testOb1", testOb1.pos, Vector3f(0f, 0f, 0f))

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(1f,1f,1f)
        testOb1.scale = Vector3f(1f)

        val testOb02 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, -1f), Vector3f(1f), Vector3f(-5f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb02) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb02", testOb02, Vector3f(1.5f, 1.5f, 1.5f))
        assertEquals("testOb02 testOb1", Vector3f(-0.5f, -0.5f, -0.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(5f,5f,5f)
        testOb1.scale = Vector3f(2f)

        val testOb03 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, -1f), Vector3f(2f), Vector3f(-5f, -5f, -5f), GravityProperties.nothing, true)
        with(testOb03) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb03", testOb03, Vector3f(1.5f, 1.5f, 1.5f))
        assertEquals("testOb03 testOb1", Vector3f(-2.5f, -2.5f, -2.5f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(-2.5f,-2.5f,-2.5f)
        testOb1.scale = Vector3f(1f)

        val testOb04 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1f, -1f, -1f), Vector3f(1f), Vector3f(5f, 5f, 5f), GravityProperties.nothing, true)
        with(testOb04) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb04", testOb04, Vector3f(-5f/3f, -5f/3f, -5f/3f))
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.x,0.001f)
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.y,0.001f)
        assertEquals("testOb04 testOb1", 1f/3f, testOb1.pos.z,0.001f)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,0f,5f)
        testOb1.scale = Vector3f(1f)

        val testOb05 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(5f, 5f, 0f), GravityProperties.nothing, true)
        with(testOb05) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb05", testOb05, Vector3f(-2f, -2f, 0f))
        assertEquals("testOb05 testOb1", Vector3f(0f, 0f, -2f), testOb1.pos)

        testOb1.pos = Vector3f(0f)
        testOb1.velocity = Vector3f(0f,-5f,0f)
        testOb1.scale = Vector3f(1f)

        val testOb06 = TestApplierObject(sap.idCounter, 1f, Vector3f(1f, 1f, 1f), Vector3f(1f), Vector3f(-5f, 0f, -5f), GravityProperties.nothing, true)
        with(testOb06) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb06", testOb06, Vector3f(2f, 1f, 2f))
        assertEquals("testOb06 testOb1", Vector3f(0f, 3f, 0f), testOb1.pos)

        testOb1.velocity = Vector3f(3f, 3f, 3f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)

        val testOb23 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(6f, 6f, 6f), GravityProperties.nothing, true)
        with(testOb23) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        testApplierObject("testOb23", testOb23, Vector3f(-1f -1f/3f, -1f -1f/3f, -1f -1f/3f))
        assertEquals("testOb23 testOb1", testOb1.pos, Vector3f(2f/3f, 2f/3f, 2f/3f))

        testOb1.velocity = Vector3f(0f)
        testOb1.pos = Vector3f(0f)
        testOb1.scale = Vector3f(1f)
    }

    @Test
    fun simpleCollisionXAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val abstractCollisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0.2f, 0f, 0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(2f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0f), testOb1.velocity)
            assertEquals(Vector3f(0.2f, 0f, 0f), testOb2.velocity)
        }
    }

    @Test
    fun simpleCollisionYAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val abstractCollisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,0f), Vector3f(1f), Vector3f(0f,0.2f,0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f,2f,0f), Vector3f(1f), Vector3f(0f,0f,0f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0f), testOb1.velocity)
            assertEquals(Vector3f(0f,0.2f,0f), testOb2.velocity)
        }
    }

    @Test
    fun simpleCollisionZAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val abstractCollisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,0f), Vector3f(1f), Vector3f(0f,0f,0.2f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f,0f,2f), Vector3f(1f), Vector3f(0f,0f,0f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0f), testOb1.velocity)
            assertEquals(Vector3f(0f,0f,0.2f), testOb2.velocity)
        }
    }

    @Test
    fun bounceOfCollisionXAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val abstractCollisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0.2f, 0f, 0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter, 1f, Vector3f(2f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                abstractCollisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                abstractCollisionHandler.handleCollision()
            }

            assertEquals(Vector3f(-0.2f, 0f, 0f), testOb1.velocity )
            assertEquals(Vector3f(0f), testOb2.velocity)
        }
    }

    @Test
    fun bounceOfCollisionYAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val collisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,0f), Vector3f(1f), Vector3f(0f,0.2f,0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f,2f,0f), Vector3f(1f), Vector3f(0f,0f,0f), GravityProperties.nothing, false)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0f,-0.2f,0f), testOb1.velocity)
            assertEquals(Vector3f(0f), testOb2.velocity)
        }
    }

    @Test
    fun bounceOfCollisionZAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val collisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,0f,0f), Vector3f(1f), Vector3f(0f,0f,0.2f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f,0f,2f), Vector3f(1f), Vector3f(0f,0f,0f), GravityProperties.nothing, false)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0f,0f,-0.2f), testOb1.velocity)
            assertEquals(Vector3f(0f), testOb2.velocity)
        }
    }

    @Test
    fun diagonalBounceOfCollisionXAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val collisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(3f, 1.5f, 0f), Vector3f(1f), Vector3f(-0.2f, -0.2f, 0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter, 1f, Vector3f(-3f, 0f, -1.5f), Vector3f(1f), Vector3f(0.2f, 0f, 0.2f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb3 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
            with(testOb3) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
            }

            assertEquals(Vector3f(0.2f, -0.2f, 0f), testOb1.velocity)
            assertEquals(Vector3f(-0.2f, 0f, 0.2f), testOb2.velocity)
        }
    }

    @Test
    fun diagonalBounceOfCollisionYAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val collisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(1.5f, 3f, 0f), Vector3f(1f), Vector3f(-0.2f, -0.2f, 0f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f,-3f, -1.5f), Vector3f(1f), Vector3f( 0f, 0.2f, 0.2f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb3 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
            with(testOb3) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
            }

            assertEquals(Vector3f(-0.2f, 0.2f,0f), testOb1.velocity)
            assertEquals(Vector3f(0f,-0.2f,0.2f), testOb2.velocity)
        }
    }

    @Test
    fun diagonalBounceOfCollisionZAxis() {
        runBlocking {
            val sap = SAP()
            val gravityManager = GravityManager()
            val collisionHandler = CollisionHandler()

            val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 1.5f, 3f), Vector3f(1f), Vector3f(0f, -0.2f, -0.2f), GravityProperties.nothing, true)
            with(testOb1) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb2 = TestApplierObject(sap.idCounter, 1f, Vector3f(-1.5f, 0f, -3f), Vector3f(1f), Vector3f( 0.2f, 0f, 0.2f), GravityProperties.nothing, true)
            with(testOb2) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }

            val testOb3 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, false)
            with(testOb3) {
                sap.insertBox(this)
                gravityManager.add(this)
                collisionHandler.add(this)
            }


            repeat(100) {
                gravityManager.applyGravity()
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
            }

            assertEquals(Vector3f( 0f,-0.2f,0.2f), testOb1.velocity)
            assertEquals(Vector3f(0.2f,0f,-0.2f), testOb2.velocity)
        }
    }



}