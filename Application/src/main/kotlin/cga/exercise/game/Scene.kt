package cga.exercise.game

import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.Atmosphere
import cga.exercise.components.geometry.atmosphere.AtmosphereMaterial
import cga.exercise.components.geometry.hitbox.HitBoxRendererInstancing
import cga.exercise.components.geometry.hitbox.IHitBoxRenderer
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.material.OverlayMaterial
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.geometry.skybox.Skybox
import cga.exercise.components.geometry.skybox.SkyboxPerspective
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.gui.*
import cga.exercise.components.properties.applier.AbstractCollisionHandler
import cga.exercise.components.properties.applier.CollisionHandler
import cga.exercise.components.properties.applier.ParallelCollisionHandler
import cga.exercise.components.properties.collision.AbstractSAP
import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.collision.ParallelSAP
import cga.exercise.components.properties.collision.SAP
import cga.exercise.components.properties.gravity.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.Moon
import cga.exercise.components.spaceObjects.Planet
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.Pages.MainGuiPage
import cga.exercise.game.Pages.MainMenuPage
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import kotlinx.coroutines.*
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SceneStats{
    companion object{
        var windowWidth : Int = 0
        var windowHeight : Int = 0
        var mousePosition : Vector2f = Vector2f()
        var mouseKeyPressed : Pair<Boolean,Boolean> = false to false
        var mouseScroll : Int = 0
    }

}

@OptIn(DelicateCoroutinesApi::class)
class Scene(private val window: GameWindow) {

//    //Shader
    private val mainShader: ShaderProgram = ShaderProgram("assets/shaders/main_vert.glsl", "assets/shaders/main_frag.glsl")
    private val skyBoxShader: ShaderProgram = ShaderProgram("assets/shaders/skyBox_vert.glsl", "assets/shaders/skyBox_frag.glsl")
    private val atmosphereShader: ShaderProgram = ShaderProgram("assets/shaders/atmosphere_vert.glsl", "assets/shaders/atmosphere_frag.glsl")
    private val spaceObjectShader : ShaderProgram = ShaderProgram("assets/shaders/spaceObject_vert.glsl", "assets/shaders/spaceObject_frag.glsl")

    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")
    private var gameState = RenderCategory.Gui

//-------------------------------------------------------------------------------------------------------------------------------------------------------

    // camera
    private val firstPersonCamera = FirstPersonCamera()

    var camera : Camera = firstPersonCamera

    private var movingObject : Transformable = camera

    private var skyboxRenderer = Skybox(20000.0f, listOf(
        "assets/textures/skybox/BluePinkNebular_right.png",
        "assets/textures/skybox/BluePinkNebular_left.png",
        "assets/textures/skybox/BluePinkNebular_bottom.png",
        "assets/textures/skybox/BluePinkNebular_top.png",
        "assets/textures/skybox/BluePinkNebular_back.png",
        "assets/textures/skybox/BluePinkNebular_front.png"
    ))

    val moonMaterial = Material(
        Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        Texture2D("assets/textures/planets/moon_emit.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        32f
    )

    val earthMaterial = OverlayMaterial(
        Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        Texture2D("assets/textures/planets/earth_emit.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        Texture2D("assets/textures/planets/earth_clouds.png",true).setTexParams( GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR),
        64f
    )

    private val earth = Planet(
        "earth",
        30f, Vector3f(2500f),0f,5.00f, Vector3f(2f,40f,0f),
        earthMaterial,
        Atmosphere( 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(70,105,208, 50))),
        null,
        listOf(Moon(0.27f,Vector3f(500f,0f,0f),0.001f,0.0001f,Vector3f(45.0f, 0f,0f), moonMaterial, Renderable( ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))),
        Renderable( ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)
    )

    private val guiRenderer = GuiRenderer(guiShader)

    private val startButtonOnClick :((Int, Int) -> Unit)= { _, _ ->
        useTestScript = false
        applySettings(mainMenu.settings)
    }

    private var useTestScript = false
    private var testFile : File? = null
    private val autoTesterButtonOnClick :((Int, Int) -> Unit)= { _, _ ->
        if(mainMenu.testScript != null){
            useTestScript = true

            testFile = File(mainMenu.testScript!!.testResultPath + "result${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmss"))}.txt")
            applyTester(mainMenu.testScript)
        }
    }

    private val mainMenu = MainMenuPage(startButtonOnClick, autoTesterButtonOnClick)
    private val mainGui = MainGuiPage()

    private val removeHitBox : ((Int) -> Unit) = {id -> sap.remove(id); hitBoxRenderer.removeHitBoxID(id); gravityContainer.remove(id); collisionHandler.remove(id)
        mainGui.objectCount = hitBoxRenderer.hitboxes.size
    }
    private val addHitBox : ((mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f) -> Unit) = { mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f ->
        with(GravityHitBox(IHitBox.idCounter, mass, GravityProperties.adopter, pos, scale, velocity)){sap.insertBox(this); hitBoxRenderer.add(this); gravityContainer.add(this); collisionHandler.add(this)}
        mainGui.objectCount = hitBoxRenderer.hitboxes.size
    }

    private var sap : AbstractSAP = SAP()
    private var hitBoxRenderer : IHitBoxRenderer = HitBoxRendererInstancing()
    private var gravityContainer : AbstractGravityManager = GravityManager()
    private var collisionHandler : AbstractCollisionHandler = CollisionHandler(removeHitBox, addHitBox)

    //scene setup
    init {

         //initial opengl state
         glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

         glEnable(GL_CULL_FACE); GLError.checkThrow()
         glFrontFace(GL_CCW); GLError.checkThrow()
         glCullFace(GL_BACK); GLError.checkThrow()

         glEnable(GL_DEPTH_TEST); GLError.checkThrow()
         glDepthFunc(GL_LESS); GLError.checkThrow()

//            camera.translateLocal(Vector3f(3f * 20f,0f,60f))

        camera.rotateLocal(-10f,-100f,45f)

//            GlobalScope.launch {
//                delay(1000)
//                gameState = mutableListOf(RenderCategory.FirstPerson)
//            }

         mainMenu.refresh()
         mainGui.refresh()
    }

    var testerId = 0
    var testerCycleCount = 0L
    private fun applyTester(testScript: Tester?){
        if (testScript == null)
            return

        if(testScript.cycleSettings.count() - 1 >= testerId){
            testerCycleCount = testScript.cycleCount
            applySettings(testScript.cycleSettings[testerId])
            testerId++
        }else{
            testerId = 0
            useTestScript = false
            exitToMenu()
        }
    }

    private fun printTesterResults(testScript: Tester?){
        if (testScript == null)
            return

        testFile?.appendText("${testerId -1}, " +
                "${testScript.cycleSettings[testerId - 1].objectCount}, "+
                "%.2f, ".format(sumFPS / counterFPS) +
                "\n")
    }

    private fun applySettings(settings: Settings){
        mainGui.objectCount = settings.objectCount
        mainGui.executeParallel = settings.executeParallel

        window.m_updatefrequency = settings.updateFrequency.toFloat()

        if(settings.executeParallel) {
            sap = ParallelSAP(settings.jobCount)
            gravityContainer = ParallelGravityManager(settings.jobCount)
            collisionHandler = ParallelCollisionHandler(settings.jobCount, removeHitBox, addHitBox)
        }else {
            sap = SAP()
            gravityContainer = GravityManager()
            collisionHandler = CollisionHandler(removeHitBox, addHitBox)
        }

        collisionHandler.seed = 85
        collisionHandler.scatterAmount = 20

//        val gravityHitBoxes = if(settings.useSampleData){
//
//            val file = File("assets/sampleData/sampleData.txt")
//            val lines = file.readLines()
//
//            MutableList<GravityHitBox>(settings.objectCount){
//                val data = lines[it].split(", ").map { it.toFloat() }
//
//                val pos = Vector3f(data[0], data[1], data[2])
//                val scale = Vector3f(1f)
//                val mass = 1f
//                val velocity = Vector3f(data[3], data[4], data[5])
//                GravityHitBox(sap.idCounter, mass, pos, scale, velocity)
//            }
//        }
//        else {
//            MutableList<GravityHitBox>(settings.objectCount) {
//                val pos = Vector3f(Random.nextInt(1, 5001).toFloat(),Random.nextInt(1, 5001).toFloat(),Random.nextInt(1, 5001).toFloat())
//                val scale = Vector3f(1f)
//                val mass = 1f
//                val velocity = Vector3f((Random.nextFloat() - 0.5f) * 10,(Random.nextFloat() - 0.5f) * 10,(Random.nextFloat() - 0.5f) * 10)
//                GravityHitBox(sap.idCounter, mass, pos, scale, velocity)
//            }
//        }
//
//        @Suppress("UNCHECKED_CAST")
//        hitBoxRenderer = HitBoxRendererInstancing(gravityHitBoxes as MutableList<HitBox>)
//
//        @Suppress("UNCHECKED_CAST")
//        gravityContainer.setAll( gravityHitBoxes as MutableList<IGravity>, GravityProperties.adopter)
//
//        @Suppress("UNCHECKED_CAST")
//        sap.setAllBoxes(gravityHitBoxes as MutableList<IHitBox>)

//        val mainGravityObject = GravityHitBox(sap.idCounter, 4000f, Vector3f(2500f), Vector3f(430f))
//        gravityContainer.add(mainGravityObject, GravityProperties.source)
//        hitBoxRenderer.add(mainGravityObject)
//        sap.insertBox(mainGravityObject)

        val h1 = GravityHitBox(IHitBox.idCounter, 0.1f, GravityProperties.nothing, Vector3f(0f, 0f, 0f), Vector3f(1f,1f,1f), velocity = Vector3f(-0.5f,0f,0f))
        with(h1){
            gravityContainer.add(this)
            hitBoxRenderer.add(this)
            sap.insertBox(this)
            collisionHandler.add(this)
        }
        val h2 = GravityHitBox(IHitBox.idCounter, 0.1f, GravityProperties.nothing, Vector3f(-5f, 0f, 0f), Vector3f(1f,1f,1f), velocity = Vector3f(0.5f,0f,0f))
        with(h2) {
            gravityContainer.add(this)
            hitBoxRenderer.add(this)
            sap.insertBox(this)
            collisionHandler.add(this)
        }

        val h3 = GravityHitBox(IHitBox.idCounter, 0.1f, GravityProperties.nothing, Vector3f(-100f, 0f, 0f), Vector3f(1f,1f,1f), velocity = Vector3f(2f,0f,0f))
        with(h3) {
            gravityContainer.add(this)
            hitBoxRenderer.add(this)
            sap.insertBox(this)
            collisionHandler.add(this)
        }

//        with(GravityHitBox(sap.idCounter, 0.1f, GravityProperties.sourceAndAdopter, Vector3f(4f, 0f, 0f), Vector3f(1f,1f,1f), velocity = Vector3f(0f,0f,0f))) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }

//        repeat(10){
//            val test01Object = GravityHitBox(sap.idCounter, 0.1f, GravityProperties.sourceAndAdopter, Vector3f(Random.nextInt(-20, 20).toFloat(),Random.nextInt(-20, 20).toFloat(),Random.nextInt(-20, 20).toFloat()), Vector3f(4f))
//            gravityContainer.add(test01Object)
//            hitBoxRenderer.add(test01Object)
//            sap.insertBox(test01Object)
//            applier.add(test01Object)
//        }

        /* Line Test*/
//        with(GravityHitBox(sap.idCounter, 1f, GravityProperties.nothing, Vector3f(4f, 0f, 0f), velocity = Vector3f(0.2f,0f,0f))) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }
//
//        with(GravityHitBox(sap.idCounter, 1f, GravityProperties.nothing, Vector3f(-4f, 0f, 0f), velocity = Vector3f(0f,0f,0f))) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }
//        with(GravityHitBox(sap.idCounter, 1f, GravityProperties.nothing, Vector3f(-6f, 0f, 0f), velocity = Vector3f(0f,0f,0f))) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }
//
//        val test3Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(-15f,0f,0f), Vector3f(1f,5f,5f), interact = false)
//        gravityContainer.add(test3Object)
//        hitBoxRenderer.add(test3Object)
//        sap.insertBox(test3Object)
//        applier.add(test3Object)
//
//        val test4Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(15f,0f,0f), Vector3f(1f,5f,5f), interact = false)
//        gravityContainer.add(test4Object)
//        hitBoxRenderer.add(test4Object)
//        sap.insertBox(test4Object)
//        applier.add(test4Object)


        /* Box Test*/
/*
//        repeat(10){
//            val test01Object = GravityHitBox(sap.idCounter, 0.1f, GravityProperties.nothing, Vector3f(Random.nextInt(-17, 17).toFloat(),Random.nextInt(-17, 17).toFloat(),Random.nextInt(-17, 17).toFloat()), velocity = Vector3f(Random.nextFloat() *0.5f,Random.nextFloat() *0.5f,Random.nextFloat() *0.5f))
//            gravityContainer.add(test01Object)
//            hitBoxRenderer.add(test01Object)
//            sap.insertBox(test01Object)
//            applier.add(test01Object)
//        }
//
//        val test3Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(-20.1f,0f,0f), Vector3f(1f,20f,20f), interact = false)
//        gravityContainer.add(test3Object)
//        hitBoxRenderer.add(test3Object)
//        sap.insertBox(test3Object)
//        applier.add(test3Object)
//
//        val test4Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(20.1f,0f,0f), Vector3f(1f,20f,20f), interact = false)
//        gravityContainer.add(test4Object)
//        hitBoxRenderer.add(test4Object)
//        sap.insertBox(test4Object)
//        applier.add(test4Object)
//
//        val test5Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,-20.1f,0f), Vector3f(20f,1f,20f), interact = false)
//        gravityContainer.add(test5Object)
//        hitBoxRenderer.add(test5Object)
//        sap.insertBox(test5Object)
//        applier.add(test5Object)
//
//        val test6Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,20.1f,0f), Vector3f(20f,1f,20f), interact = false)
//        gravityContainer.add(test6Object)
//        hitBoxRenderer.add(test6Object)
//        sap.insertBox(test6Object)
//        applier.add(test6Object)
//
//        val test7Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,0f,-20.1f), Vector3f(20f,20f,1f), interact = false)
//        gravityContainer.add(test7Object)
//        hitBoxRenderer.add(test7Object)
//        sap.insertBox(test7Object)
//        applier.add(test7Object)
//
//        val test8Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f, 0f, 20.1f), Vector3f(20f,20f,1f), interact = false)
//        gravityContainer.add(test8Object)
//        hitBoxRenderer.add(test8Object)
//        sap.insertBox(test8Object)
//        applier.add(test8Object)
*/
        //Hit Test
/*
//        with(GravityHitBox(sap.idCounter, 1f, GravityProperties.nothing, Vector3f(0f, 0f, 0f), Vector3f(1f), velocity = Vector3f(5f,0f,0f), interact = true)) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }
//

//        with(GravityHitBox(sap.idCounter, 1f, GravityProperties.nothing, Vector3f(5f, 0f, 0f), Vector3f(1f), velocity = Vector3f(-1f, 1f,0f), interact = true)) {
//            gravityContainer.add(this)
//            hitBoxRenderer.add(this)
//            sap.insertBox(this)
//            applier.add(this)
//        }

//        val test3Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(-21.1f,0f,0f), Vector3f(1f,20f,20f), interact = false)
//        gravityContainer.add(test3Object)
//        hitBoxRenderer.add(test3Object)
//        sap.insertBox(test3Object)
//        applier.add(test3Object)
//
//        val test4Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(21.1f,0f,0f), Vector3f(1f,20f,20f), interact = false)
//        gravityContainer.add(test4Object)
//        hitBoxRenderer.add(test4Object)
//        sap.insertBox(test4Object)
//        applier.add(test4Object)
//
//        val test5Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,-21.1f,0f), Vector3f(20f,1f,20f), interact = false)
//        gravityContainer.add(test5Object)
//        hitBoxRenderer.add(test5Object)
//        sap.insertBox(test5Object)
//        applier.add(test5Object)
//
//        val test6Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,21.1f,0f), Vector3f(20f,1f,20f), interact = false)
//        gravityContainer.add(test6Object)
//        hitBoxRenderer.add(test6Object)
//        sap.insertBox(test6Object)
//        applier.add(test6Object)
//
//        val test7Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,0f,-21.1f), Vector3f(20f,20f,1f), interact = false)
//        gravityContainer.add(test7Object)
//        hitBoxRenderer.add(test7Object)
//        sap.insertBox(test7Object)
//        applier.add(test7Object)
//
//        val test8Object = GravityHitBox(sap.idCounter, 999f, GravityProperties.nothing, Vector3f(0f,0f,21.1f), Vector3f(20f,20f,1f), interact = false)
//        gravityContainer.add(test8Object)
//        hitBoxRenderer.add(test8Object)
//        sap.insertBox(test8Object)
//        applier.add(test8Object)
*/
        hitBoxRenderer.updateModelMatrix()

        runBlocking {
            sap.sort()
            sap.checkCollision()
        }

        hitBoxRenderer.updateModelMatrix()

        sumFPS = 0f
        counterFPS = 0

        gameState = RenderCategory.FirstPerson
        window.setCursorVisible(false)
    }

    private fun exitToMenu(){
        sap.clear()
        hitBoxRenderer.cleanup()
        hitBoxRenderer.clear()
        gravityContainer.clear()

        gameState = RenderCategory.Gui
        window.setCursorVisible(true)
    }

    private var frameCounter = 0
    private var lastT = 0f

    private var sumFPS = 0f
    private var counterFPS = 0L


    private var renderCount = 0L
    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (t - lastT  > 0.05f){
            val fps = frameCounter / (t - lastT)
            mainGui.fpsGuiElement.text = "%.1f".format(fps)
            frameCounter = 0
            lastT = t

            sumFPS += fps
            counterFPS++

            renderCount++
        }
        frameCounter++


        if(gameState == RenderCategory.FirstPerson) {

//            mainShader.use()

//            if (t - lastTime > 0.01f)
//                mainShader.setUniform("time", t)



//            camera.bind(mainShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//            earth.render(mainShader)


            camera.bind(spaceObjectShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
            hitBoxRenderer.render(spaceObjectShader)

            //-- SkyBoxShader
            SkyboxPerspective.bind(skyBoxShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
            skyboxRenderer.render(skyBoxShader)
            //--

            //-- AtmosphereShader
//            atmospherePerspective.bind(atmosphereShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//                earth.atmosphere?.render(atmosphereShader)
            //--
        }

        guiRenderer.beforeGUIRender()

        //-- GuiRenderer
        if(gameState == RenderCategory.FirstPerson)
            guiRenderer.render(mainGui, dt, t)
        else
            guiRenderer.render(mainMenu, dt, t)
        //--


        guiRenderer.afterGUIRender()

//        if(t - lastTime > 0.5f)
//            lastTime = t



        if (useTestScript){
            if(renderCount > testerCycleCount ){
                printTesterResults(mainMenu.testScript)
                applyTester(mainMenu.testScript)
                renderCount = 0
            }
        }
    }

    private var updateCounter = 0
    private var updateLastT = 0f

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun update(dt: Float, t: Float) {
        if (t - updateLastT  > 0.5f){
            mainGui.upsGuiElement.text = "%.0f".format(updateCounter / ( t - updateLastT))
            updateLastT = t
            updateCounter = 0
        }
        updateCounter++

        if(gameState == RenderCategory.FirstPerson){
//            gravityContainer.applyGravity()
//            hitBoxRenderer.updateModelMatrix()
//            sap.sort()
//            sap.checkCollision()
//            earth.orbit()
//            applier.checkBounceOff()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun updateUI(dt: Float, t: Float) {
        if(gameState == RenderCategory.Gui)
            mainMenu.globalOnUpdateEvent(dt, t)

        val rotationMultiplier = 30f
        val translationMultiplier = 35.0f

//        if (window.getKeyState(GLFW_KEY_Q)) {
//            movingObject.rotateLocal(rotationMultiplier * dt, 0.0f, 0.0f)
//        }
//
//        if (window.getKeyState(GLFW_KEY_E)) {
//            movingObject.rotateLocal(-rotationMultiplier  * dt, 0.0f, 0.0f)
//        }

        if (window.getKeyState ( GLFW_KEY_W) && !window.getKeyState ( GLFW_KEY_T)) {
            movingObject.translate(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
            //spaceship.activateMainThrusters()
        }

        if (window.getKeyState ( GLFW_KEY_S)) {
            movingObject.translate(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
        }
//
//        if (window.getKeyState ( GLFW_KEY_G)) {
//            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 10))
//        }

        if (gameState == RenderCategory.FirstPerson){
            if (window.getKeyState ( GLFW_KEY_T))
                movingObject.translate(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 15))

            if (window.getKeyState ( GLFW_KEY_A))
                movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)

            if (window.getKeyState ( GLFW_KEY_D))
                movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
        }

//        if (gameState.contains(RenderCategory.ThirdPerson)){
//            if (window.getKeyState ( GLFW_KEY_A)) {
//                movingObject.rotateLocal(0.0f, rotationMultiplier * dt, 0.0f)
//                spaceship.activateRightTurnThruster()
//            }
//
//            if (window.getKeyState ( GLFW_KEY_D)) {
//                movingObject.rotateLocal(0.0f, -rotationMultiplier * dt, 0.0f)
//                spaceship.activateLeftTurnThruster()
//            }
//        }
        SceneStats.mouseScroll = 0
    }

    private var lastCameraMode = gameState
    private var lastCamera = camera

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        //println("key:$key scancode:$scancode action:$action mode:$mode")

        if(gameState == RenderCategory.Gui && (action == 1 || action == 2))
            mainMenu.focusedElement?.onKeyDown?.let { it(key, scancode, mode) }

        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS){
            when(gameState){
                RenderCategory.Gui -> {
                    window.quit()
                }
                RenderCategory.FirstPerson -> {
                    exitToMenu()
                }
            }
        }

        if (key == GLFW_KEY_G && (action == 1 || action == 2))
        {

            runBlocking {
                sap.sort()
                sap.checkCollision()
                collisionHandler.handleCollision()
                hitBoxRenderer.updateModelMatrix()
                gravityContainer.applyGravity()
                earth.orbit()
            }


//            println("--------")
//            println("${collisionHandler.hitBoxes[0].getPosition().x} ${collisionHandler.hitBoxes[0].getPosition().y}")
//            println("${collisionHandler.hitBoxes[0].velocity.x} ${collisionHandler.hitBoxes[0].velocity.y}")
//            println()
//            println("${collisionHandler.hitBoxes[1].getPosition().x} ${collisionHandler.hitBoxes[1].getPosition().y}")
//            println("${collisionHandler.hitBoxes[1].velocity.x} ${collisionHandler.hitBoxes[1].velocity.y}")
        }
    }

    fun onMouseButton(button: Int, action: Int, mode: Int) {
        if(action == 1 && gameState == RenderCategory.Gui){
            mainMenu.globalClickEvent(button, action, Vector2f(mouseXPos, mouseYPos))
        }

        when(button){
            0 -> when(action){
                1 -> SceneStats.mouseKeyPressed = true to SceneStats.mouseKeyPressed.second
                0 -> SceneStats.mouseKeyPressed = false to SceneStats.mouseKeyPressed.second
            }
            1 -> when(action) {
                1 -> SceneStats.mouseKeyPressed = SceneStats.mouseKeyPressed.first to true
                0 -> SceneStats.mouseKeyPressed = SceneStats.mouseKeyPressed.first to true
            }
        }

    }

    var mouseXPos = 0f
    var mouseYPos = 0f
    var oldXpos : Double = 0.0
    var oldYpos : Double = 0.0

    fun onMouseMove(xPos: Double, yPos: Double) {

        mouseXPos = xPos.toFloat()
        mouseYPos = window.windowHeight - yPos.toFloat()
        SceneStats.mousePosition = Vector2f(mouseXPos, mouseYPos)

        if (gameState == RenderCategory.FirstPerson){
            camera.rotateLocal((oldYpos-yPos).toFloat()/20.0f, (oldXpos-xPos).toFloat()/20.0f, 0f)
        }

        oldXpos = xPos
        oldYpos = yPos
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
        SceneStats.mouseScroll += yoffset.toInt()
    }

    fun onWindowSize(width: Int, height: Int) {
        if (width == 0 || height == 0)
            return

        mainMenu.refresh()
        mainGui.refresh()
    }

    fun shutdown(){
        Settings.saveSettings(mainMenu.settings)
    }

    fun cleanup() {
        mainMenu.cleanup()
        mainGui.cleanup()

        guiRenderer.cleanup()
        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()
    }
}
