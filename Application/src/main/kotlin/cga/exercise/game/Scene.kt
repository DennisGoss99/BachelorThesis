package cga.exercise.game

import cga.exercise.components.gui.Color
import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.camera.ThirdPersonCamera
import cga.exercise.components.camera.ZoomCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.*
import cga.exercise.components.geometry.mesh.*
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.mapGenerator.MapGeneratorMaterials
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.*
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import kotlinx.coroutines.*
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE

class SceneStats{
    companion object{
        var windowWidth : Int = 0
        var windowHeight : Int = 0
        var mousePosition : Vector2f = Vector2f()
        var mouseKeyPressed : Pair<Boolean,Boolean> = false to false
    }
}

class Scene(private val window: GameWindow) {



//    //Shader
    private val mainShader: ShaderProgram = ShaderProgram("assets/shaders/main_vert.glsl", "assets/shaders/main_frag.glsl")
    private val skyBoxShader: ShaderProgram = ShaderProgram("assets/shaders/skyBox_vert.glsl", "assets/shaders/skyBox_frag.glsl")
    private val atmosphereShader: ShaderProgram = ShaderProgram("assets/shaders/atmosphere_vert.glsl", "assets/shaders/atmosphere_frag.glsl")
    private val particleShader: ShaderProgram = ShaderProgram("assets/shaders/particle_vert.glsl", "assets/shaders/particle_frag.glsl")

    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")
    private val fontShader: ShaderProgram = ShaderProgram("assets/shaders/font_vert.glsl", "assets/shaders/font_frag.glsl")

    private var gameState = mutableListOf(RenderCategory.PressToPlay)

    private val renderAlways = RenderCategory.values().toList()

//-------------------------------------------------------------------------------------------------------------------------------------------------------

    // camera
    private val firstPersonCamera = FirstPersonCamera()
    private val thirdPersonCamera = ThirdPersonCamera()

    private val zoomCamera = ZoomCamera()

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

//    private val animatedGuiElement = LoopAnimatedGuiElement(Animator(0.4f, listOf(Vector2f(0.0f, -0.4f),Vector2f(0.0f, -0.5f))),"assets/textures/gui/PressKeyToPlay.png", 1, listOf(RenderCategory.PressToPlay), Vector2f(0.4f,0.4f))
//
//    private val loadingBarGuiElement = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 2, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)
//    private val loadingBarGuiElement2 = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 3, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)
//    private val loadingBarGuiElement3 = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 4, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)
//
//    private val animatedHelpScreen = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f( 0.6f, 1.5f) to 1.5f ,Vector2f(0.6f) to 0f)),"assets/textures/gui/HelpScreen.png", 2, renderHelpScreen, Vector2f(0.4f))

//    private val speedDisplay = GuiElement("assets/textures/gui/SpeedSymbols.png" , 1, renderMainGame, Vector2f(0.1f,0.1f),Vector2f(-0.85f,0.9f))
//    private val speedMarker = SpeedMarker(0,"assets/textures/gui/SpeedMarker.png",0, renderMainGame, Vector2f(1f,1f), parent = speedDisplay)

    val f1 = {_: Int, _: Int -> println("Button 1") }
    val f2 = {_: Int, _: Int -> println("Button 2") }

    private val earth = Planet(
        "earth",
        1f,3f,0f,0.00f, Vector3f(2f,20f,0f),
        MapGeneratorMaterials.earthMaterial,
        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(70,105,208, 50))),
        null,
        listOf(Moon(0.27f,8f,0.001f,0.0001f,Vector3f(45.0f, 0f,0f), MapGeneratorMaterials.moonMaterial, Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    val guiRenderer = GuiRenderer(guiShader, fontShader)

//    val testGuiElement = Box(AspectRatio(),Relative(1f), Center(), Center(), cornerRadius = 10,
//        children = listOf(
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(-0.5f)),
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(0.5f))
//        )
//    )

//    val testGuiElement =  Box(Relative(0.75f),Relative(0.5f), Center(), Center(), Color(255,128,0),
//        children = listOf(
//            Button("Button 1", PixelWidth(200), PixelHeight(80), Center(), PixelTop(20), onClick = f1),
//            Button("Button 2", PixelWidth(200), PixelHeight(80), Center(), PixelBottom(20), onClick = f2),
//        )
//    )

    val testGuiElement = Box(Relative(0.7f), Relative(0.9f), PixelLeft(20), Center(),onFocus = {->} , children = listOf(
        Box(Relative(0.5f), Relative(0.7f), PixelLeft(50), PixelTop(50), Color(255,128,0), cornerRadius = 10, onFocus = {->} ,
            children = listOf(
                Textbox("Center\nHallo", PixelWidth(200), PixelHeight(80), Center(), PixelTop(20)),
                Textbox("Right", PixelWidth(200), PixelHeight(80), Center(), Center(), textMode = TextMode.Right),
                Textbox("Left", PixelWidth(200), PixelHeight(80), Center(), PixelBottom(20), textMode = TextMode.Left),
            )
        )
    ))

    private val fpsGuiElement = Text("",6f, StaticResources.standardFont,30f, TextMode.Left,false, PixelLeft(0), PixelTop(0), color = Color(255f,255f,255f))

    //scene setup
    init {
         runBlocking {

            //initial opengl state
            glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

            glEnable(GL_CULL_FACE); GLError.checkThrow()
            glFrontFace(GL_CCW); GLError.checkThrow()
            glCullFace(GL_BACK); GLError.checkThrow()

            glEnable(GL_DEPTH_TEST); GLError.checkThrow()
            glDepthFunc(GL_LESS); GLError.checkThrow()

//            GlobalScope.launch {
//                delay(1000)
//                gameState = mutableListOf(RenderCategory.FirstPerson)
//            }


    //        //configure LoadingBar
    //        loadingBarGuiElement2.setPosition(Vector2f(0.1f, 0f))
    //        loadingBarGuiElement3.setPosition(Vector2f(0.2f, 0f))

             testGuiElement.refresh()
             fpsGuiElement.refresh()
        }
    }

    var frameCounter = 0
    var lastT = 0f

    var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (t - lastT  > 0.5f){
            lastT = t
            frameCounter *= 2
            fpsGuiElement.text = "$frameCounter"
            fpsGuiElement.textHasChanged()
            frameCounter = 0
        }
        frameCounter ++
//

//        mainShader.use()
//        mainShader.setUniform("emitColor", Vector3f(0f,0.5f,1f))
////
//        if(t-lastTime > 0.01f)
//            mainShader.setUniform("time", t)
////
//
//
//        camera.bind(mainShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
////        renderables.render(gameState, mainShader)
//
//        earth.render(mainShader)
//
//
//
//        //-- SkyBoxShader
//
//        SkyboxPerspective.bind(skyBoxShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//        skyboxRenderer.render(skyBoxShader)
//        //--
////
////
////        //-- Particle
////        if(gameState.contains( RenderCategory.ThirdPerson)){
////            spaceship.bindThrusters(particleShader,camera.getCalculateProjectionMatrix(),camera.getCalculateViewMatrix())
////            spaceship.renderThrusters(particleShader)
////        }
////        //--
////
////        //-- AtmosphereShader
////        atmospherePerspective.bind(atmosphereShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
////        solarSystem.renderAtmosphere(atmosphereShader)
////        //--
//

        guiRenderer.beforeGUIRender()

        //-- GuiRenderer
            guiRenderer.render(testGuiElement, dt, t)
        //--

        //-- FPS Count
            guiRenderer.render(fpsGuiElement, dt, t)
        //--

        guiRenderer.afterGUIRender()

        if(t-lastTime > 0.01f)
            lastTime = t
    }

    fun update(dt: Float, t: Float) {

        testGuiElement.globalOnUpdateEvent(dt, t)

//        earth.orbit()
//        when(speedMarker.state){
//            1 -> solarSystem.update(dt,t)
//            2 -> {
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//            }
//        }
//        if(window.getKeyState(GLFW_KEY_SPACE)){
//            for(i in 0..15)
//                solarSystem.update(dt,t)
//        }
//
//
//        spaceship.updateThrusters(dt,t)
//
//        //Animated GUI
//
//        animatedGuiElement.update(dt,t)
//        animatedHelpScreen.update(dt,t)
//        loadingBarGuiElement.update(dt,t)
//        loadingBarGuiElement2.update(dt,t)
//        loadingBarGuiElement3.update(dt,t)
//
//
        val rotationMultiplier = 30f
        val translationMultiplier = 35.0f
//
//        if (window.getKeyState(GLFW_KEY_Q)) {
//            movingObject.rotateLocal(rotationMultiplier * dt, 0.0f, 0.0f)
//        }
//
//        if (window.getKeyState(GLFW_KEY_E)) {
//            movingObject.rotateLocal(-rotationMultiplier  * dt, 0.0f, 0.0f)
//        }
//
//
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
//
//
//
//        if (window.getKeyState ( GLFW_KEY_T)) {
//            for(i in 0..3){
//                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
//                spaceship.activateMainThrusters()
//
//                spaceship.activateRightTurnThruster()
//                spaceship.activateLeftTurnThruster()
//
//                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 1.5f))
//                spaceship.activateRightTurnThruster()
//                spaceship.activateLeftTurnThruster()
//            }
//        }
//
//        if (gameState.contains(RenderCategory.FirstPerson)){
//            if (window.getKeyState ( GLFW_KEY_A))
//                movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)
//
//            if (window.getKeyState ( GLFW_KEY_D))
//                movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
//        }
//
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
    }

    private var lastCameraMode = gameState
    private var lastCamera = camera

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        //println("key:$key scancode:$scancode action:$action mode:$mode")

        if(action == 1 || action == 2)
            testGuiElement.focusedElement?.onKeyDown?.let { it(key, scancode, mode) }

//        if(gameState.contains(RenderCategory.PressToPlay)){
//
//            gameState = mutableListOf(RenderCategory.Loading)
//
//            //Load SolarSystem
//            solarSystem = SolarSystem(
//                listOf(sun),
//                listOf(earth, mars, uranus, venus, saturn, jupiter),
//                listOf(AsteroidBelt(60, 8,12), AsteroidBelt(80, 28,34)))
//
//            gameState = mutableListOf(RenderCategory.FirstPerson)
//            return
//        }

//        if(GLFW_KEY_F5 == key && action == 0)
//            when{
//                gameState.contains(RenderCategory.FirstPerson) ->{
//                    spaceship.modelMatrix = firstPersonCamera.modelMatrix
//                    camera = thirdPersonCamera
//
//                    gameState.remove(RenderCategory.FirstPerson)
//                    gameState.add(RenderCategory.ThirdPerson)
//
//                    movingObject = spaceship
//                }
//                gameState.contains(RenderCategory.ThirdPerson) -> {
//
//                    camera = firstPersonCamera
//
//                    gameState.remove(RenderCategory.ThirdPerson)
//                    gameState.add(RenderCategory.FirstPerson)
//
//                    movingObject = camera
//                }
//            }
//
//        if(GLFW_KEY_Y == key && action == 1){
//            lastCamera = camera
//            lastCameraMode = gameState
//
//            camera = zoomCamera
//            gameState.add(RenderCategory.Zoom)
//        }
//        if(GLFW_KEY_Y == key && action == 0){
//            gameState.remove(RenderCategory.Zoom)
//            gameState = lastCameraMode
//            camera = lastCamera
//        }
//
//
//        if(GLFW_KEY_N == key && action == 0){
//            solarSystem = MapGenerator.generateSolarSystem()
//        }
//
//        if(GLFW_KEY_TAB == key && action == 0){
//            speedMarker.addToState()
//        }

    }

    fun onMouseButton(button: Int, action: Int, mode: Int) {
        if(action == 1){
            testGuiElement.globalClickEvent(button, action, Vector2f(mouseXPos, mouseYPos))
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

    fun onMouseMove(xPos: Double, yPos: Double) {

        mouseXPos = xPos.toFloat()
        mouseYPos = window.windowHeight - yPos.toFloat()
        SceneStats.mousePosition = Vector2f(mouseXPos, mouseYPos)


        // sets cursor position Vector2f(openGlMouseXPos / openGlMouseYPos)
//        cursorGuiElement.setPosition(Vector2f(((mouseXPos / window.windowWidth) * 2) -1,((mouseYPos / window.windowHeight) * 2) -1))
//        println("MouseXPos: [$mouseXPos] $xpos | MouseYPos: [$mouseYPos]")


//        when{
//            gameState.contains(RenderCategory.FirstPerson) ->{
//                camera.rotateLocal((oldYpos-yPos).toFloat()/20.0f, (oldXpos-xPos).toFloat()/20.0f, 0f)
//
//            }
//            gameState.contains(RenderCategory.ThirdPerson) -> {
//                camera.rotateAroundPoint((oldYpos-yPos).toFloat() * 0.002f , (oldXpos-xPos).toFloat() * 0.002f,0f, Vector3f(0f,0f,0f))
//            }
//        }
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
    }

    fun onWindowSize(width: Int, height: Int) {
        testGuiElement.refresh()
    }

    fun cleanup() {

        //renderables.cleanup()
        //gui.cleanup()
        testGuiElement.cleanup()
        fpsGuiElement.cleanup()

        guiRenderer.cleanup()
        fontShader.cleanup()
        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()
    }




}
