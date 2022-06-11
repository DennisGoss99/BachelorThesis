import cga.exercise.components.properties.applier.AbstractCollisionHandler
import cga.exercise.components.properties.applier.CollisionAxis
import cga.exercise.components.properties.applier.CollisionHandler
import cga.exercise.components.properties.applier.TestApplierObject
import cga.exercise.components.properties.collision.SAP
import cga.exercise.components.properties.gravity.GravityManager
import cga.exercise.components.properties.gravity.GravityProperties
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.junit.Test
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible


class GetCollisionAxisTest {

    @Test
    fun collisionAxisX(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(1f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(1f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.X, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisXNegativeVelocity(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(-0.2f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(-2f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.X, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisY(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f,1f,  0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f,1f,  0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.Y, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisYNegativeVelocity(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f,-0.2f,  0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f( 0f, -2f,0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.Y, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisZ(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f( 0f, 0f, 1f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 1f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.Z, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisZNegativeVelocity(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f( 0f, 0f, -0.2f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, -2f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.Z, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisXY(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(2f, 2f, 0f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.XY, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisXZ(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(2f,0f,2f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.XZ, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisYZ(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f,2f,  2f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.YZ, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisXYZ(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(2f,2f,  2f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.XYZ, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

    @Test
    fun collisionAxisUnknown(){

        val sap = SAP()
        val gravityManager = GravityManager()
        val abstractCollisionHandler = CollisionHandler()

        val funGetCollisionAxis = AbstractCollisionHandler::class.declaredMemberFunctions.find { it.name == "getCollisionAxis" }!!
        funGetCollisionAxis.isAccessible = true

        val testOb1 = TestApplierObject(sap.idCounter, 1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f( 1.9f, 1.9f, -1.9f), GravityProperties.nothing, true)
        with(testOb1) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        val testOb2 = TestApplierObject(sap.idCounter,1f, Vector3f(0f, 0f, 0f), Vector3f(1f), Vector3f(0f, 0f, 0f), GravityProperties.nothing, true)
        with(testOb2) {
            sap.insertBox(this)
            gravityManager.add(this)
            abstractCollisionHandler.add(this)
        }

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        Assert.assertEquals(CollisionAxis.Unknown, funGetCollisionAxis.call(abstractCollisionHandler, testOb1, testOb2))
    }

}