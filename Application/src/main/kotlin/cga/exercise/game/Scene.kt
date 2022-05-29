package cga.exercise.game

import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.collision.*
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.Atmosphere
import cga.exercise.components.geometry.atmosphere.AtmosphereMaterial
import cga.exercise.components.geometry.atmosphere.atmospherePerspective
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.material.OverlayMaterial
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.geometry.skybox.Skybox
import cga.exercise.components.geometry.skybox.SkyboxPerspective
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.gravity.GravityObjectContainer
import cga.exercise.components.gravity.GravityHitBox
import cga.exercise.components.gravity.GravityProperties
import cga.exercise.components.gui.*
import cga.exercise.components.gui.constraints.*
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
import java.util.*
import kotlin.random.Random
import kotlin.reflect.typeOf

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
//    private val particleShader: ShaderProgram = ShaderProgram("assets/shaders/particle_vert.glsl", "assets/shaders/particle_frag.glsl")
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
        mainGui.objectCount = mainMenu.objectCount
        mainGui.executeParallel = mainMenu.executeParallel

        window.m_updatefrequency = mainMenu.updateFrequency.toFloat()

        sap = if(mainMenu.executeParallel) {
            val sap = ParallelSAP()
            sap.jobCount = mainMenu.jobCount
            sap
        }else SAP()

        hitBoxes.clear()
        gravityContainer.clear()

        val mainGravityObject = GravityHitBox(HitBox(sap.idCounter),4000f)
        mainGravityObject.hitBox.translateLocal(Vector3f(2500f))
        mainGravityObject.hitBox.scaleLocal(Vector3f(430f))
        gravityContainer.add(mainGravityObject, GravityProperties.source)
        hitBoxes.add(mainGravityObject.hitBox)
        sap.insertBox(mainGravityObject.hitBox)

        if(mainMenu.useSampleData){
            var file = File("assets/sampleData/sampleData.txt")
            var counter = 0
            file.forEachLine{
                counter++
                if (counter < mainMenu.objectCount) {

                    var data = it.split(", ").map { it.toFloat() }

                    val hitBox = HitBox(sap.idCounter)
                    hitBox.translateLocal(Vector3f(data[0], data[1], data[2]))
                    val Test = GravityHitBox(hitBox, 1f, Vector3f(data[3], data[4], data[5]))
                    hitBox.updateEndPoints()
                    hitBoxes.add(hitBox)
                    gravityContainer.add(Test, GravityProperties.adopter)
                    sap.insertBox(hitBox)
                }
            }
        }
        else {
            repeat(mainMenu.objectCount){
                val hitBox = HitBox(sap.idCounter)
                hitBox.translateLocal(Vector3f(Random.nextInt(1,5001).toFloat(),Random.nextInt(1,5001).toFloat(),Random.nextInt(1,5001).toFloat()))

                val Test = GravityHitBox(hitBox,1f, Vector3f((Random.nextFloat() -0.5f) * 10, (Random.nextFloat() -0.5f) * 10, (Random.nextFloat() -0.5f) * 10))
                hitBox.updateEndPoints()
                hitBoxes.add(hitBox)
                gravityContainer.add(Test, GravityProperties.adopter)
                sap.insertBox(hitBox)
            }
        }

        hitBoxes.updateModelMatrix()

        runBlocking {
            sap.sort()
        }


        gameState = RenderCategory.FirstPerson
        window.setCursorVisible(false)
    }

    private val mainMenu = MainMenuPage(startButtonOnClick)
    private val mainGui = MainGuiPage()

    private var sap : AbstractSAP = SAP()
    private val hitBoxes = HitBoxRenderer()
    private val gravityContainer = GravityObjectContainer()

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

         camera.translateLocal(Vector3f(0f,0f,5f))

//            GlobalScope.launch {
//                delay(1000)
//                gameState = mutableListOf(RenderCategory.FirstPerson)
//            }

//        //configure LoadingBar
//        loadingBarGuiElement2.setPosition(Vector2f(0.1f, 0f))
//        loadingBarGuiElement3.setPosition(Vector2f(0.2f, 0f))

//             testGuiElement.refresh()
//             fpsGuiElement.refresh()
         mainMenu.refresh()
         mainGui.refresh()
    }

    private var frameCounter = 0
    private var lastT = 0f

    private var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (t - lastT  > 0.5f){
            lastT = t
            frameCounter *= 2
            mainGui.fpsGuiElement.text = frameCounter.toString()
            frameCounter = 0
        }
        frameCounter++

        if(gameState == RenderCategory.FirstPerson) {

            mainShader.use()

            if (t - lastTime > 0.01f)
                mainShader.setUniform("time", t)



            camera.bind(mainShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
            earth.render(mainShader)


            camera.bind(spaceObjectShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
            hitBoxes.render(spaceObjectShader)

            //-- SkyBoxShader
            SkyboxPerspective.bind(skyBoxShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
            skyboxRenderer.render(skyBoxShader)
            //--

            //-- AtmosphereShader
            atmospherePerspective.bind(atmosphereShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
                earth.atmosphere?.render(atmosphereShader)
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

        if(t-lastTime > 0.01f)
            lastTime = t
    }

    private var updateCounter = 0
    private var updateLastT = 0f

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun update(dt: Float, t: Float) {
        if (t - updateLastT  >= 0.5f){
            updateLastT = t
            updateCounter *= 2
            mainGui.upsGuiElement.text = updateCounter.toString()
            updateCounter = 0
        }
        updateCounter++



        if(gameState == RenderCategory.FirstPerson){

            if(mainMenu.executeParallel){
                gravityContainer.applyGravityParallel(4)
                hitBoxes.updateModelMatrix()
            }else{
                gravityContainer.applyGravity()
                hitBoxes.updateModelMatrix()
            }

            sap.sort()
            sap.checkCollision()

//            sap.hitBoxes.toList().forEach { hitbox ->
//                if(hitbox.collided.get()){
//                    if(hitbox.collidedWith[0].id <= 6 && hitbox.id > 6){
//                        sap.remove(hitbox)
//                        hitBoxes.removeHitBoxID(hitbox.id)
//                        gravityContainer.removeID(hitbox.id)
//                    }
//                }
//            }

            earth.orbit()
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
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
            //spaceship.activateMainThrusters()
        }

        if (window.getKeyState ( GLFW_KEY_S)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
        }
//
//        if (window.getKeyState ( GLFW_KEY_G)) {
//            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 10))
//        }

        if (gameState == RenderCategory.FirstPerson){
            if (window.getKeyState ( GLFW_KEY_T))
                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 15))

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

        if(GLFW_KEY_P == key && action == 0){
            val file = File("print/obj ${System.currentTimeMillis()}.txt")

            var text = ""

            gravityContainer.gravityObjectsApply.forEach {
                it as GravityHitBox
                text += "GravityHitBox(HitBox(sap.idCounter, Vector3f(${it.startPos.x}f, ${it.startPos.y}f, ${it.startPos.z}f)), 1f, Vector3f( ${it.startV.x}f, ${it.startV.y}f, ${it.startV.z}f))\n"
            }

            file.writeText(text)
        }

        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS){
            when(gameState){
                RenderCategory.Gui -> window.quit()
                RenderCategory.FirstPerson -> {
                    gameState = RenderCategory.Gui
                    window.setCursorVisible(true)
                }
            }
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

    fun cleanup() {
        mainMenu.cleanup()
        mainGui.cleanup()

        guiRenderer.cleanup()
        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()
    }
}
