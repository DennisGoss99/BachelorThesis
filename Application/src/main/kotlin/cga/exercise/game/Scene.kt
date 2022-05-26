package cga.exercise.game

import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.collision.SAP
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.collision.HitBox
import cga.exercise.components.collision.HitBoxRenderer
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
import cga.exercise.components.gravity.GravityProperties
import cga.exercise.components.gravity.GravityHitBox
import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
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
import kotlin.random.Random
import kotlin.system.measureNanoTime

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


/*
//    val testGuiElement = Box(AspectRatio(),Relative(1f), Center(), Center(), cornerRadius = 10,
//        children = listOf(
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(-0.5f)),
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(0.5f))
//        )
//    )

//    val testGuiElement = Box(AspectRatio(),Relative(1f), Center(), Center(), cornerRadius = 10,
//        children = listOf(
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(-0.5f)),
//            Slider(Relative(0.9f),PixelHeight(30), Center(), Relative(0.5f)),
//            ToggleButton(false, PixelWidth(45),PixelHeight(20), Center(),Center(), true)
//        )
//    )

//    val testGuiElement =  Box(Relative(0.75f),Relative(0.5f), Center(), Center(), Color(255,128,0),
//        children = listOf(
//            Button("Button 1", PixelWidth(200), PixelHeight(80), Center(), PixelTop(20), onClick = f1),
//            Button("Button 2", PixelWidth(200), PixelHeight(80), Center(), PixelBottom(20), onClick = f2),
//        )
//    )

//    private val mainMenu = Box(Relative(1f), Relative(1f), Center(), Center(), Color.black ,onFocus = {->} , children = listOf(
//
//        Box(Relative(0.5f), Relative(0.7f), PixelLeft(50), PixelTop(50), Color(255,128,0), cornerRadius = 10, onFocus = {->} ,
//            children = listOf(
//                Textbox("Center\nHallo", PixelWidth(200), PixelHeight(80), Center(), PixelTop(20)),
//                Textbox("Right", PixelWidth(200), PixelHeight(80), Center(), Center(), textMode = TextMode.Right),
//                Textbox("Left", PixelWidth(200), PixelHeight(80), Center(), PixelBottom(20), textMode = TextMode.Left),
//            )
//        )
////        Text("HABCDEFGHIJKLMNOPQRSTUVWXYZ", 5f, StaticResources.arial,30f,TextMode.Center, true, PixelLeft(0), PixelTop(0)),
//    ))
*/

    private val guiRenderer = GuiRenderer(guiShader)

    private val startButtonOnClick :((Int, Int) -> Unit)= { _, _ ->
        mainGui.objectCount = mainMenu.objectCount
        mainGui.executeParallel = mainMenu.executeParallel


        window.m_updatefrequency = mainMenu.updateFrequency.toFloat()


        sap.clear()
        hitBoxes.clear()
        gravityContainer.clear()

        val mainGravityObject = GravityHitBox(HitBox(sap.idCounter),4000f)
        mainGravityObject.hitBox.translateLocal(Vector3f(2500f))
        mainGravityObject.hitBox.scaleLocal(Vector3f(430f))
        gravityContainer.add(mainGravityObject, GravityProperties.source)
        hitBoxes.add(mainGravityObject.hitBox)
        sap.insertBox(mainGravityObject.hitBox)

        var file = File("assets/sampleData/sample1.txt")
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

//        repeat(mainMenu.objectCount){
//            val hitBox = HitBox(sap.idCounter)
//            hitBox.translateLocal(Vector3f(Random.nextInt(1,5001).toFloat(),Random.nextInt(1,5001).toFloat(),Random.nextInt(1,5001).toFloat()))
//
//            val Test = GravityHitBox(hitBox,1f, Vector3f((Random.nextFloat() -0.5f) * 10, (Random.nextFloat() -0.5f) * 10, (Random.nextFloat() -0.5f) * 10))
//            hitBox.updateEndPoints()
//            hitBoxes.add(hitBox)
//            gravityContainer.add(Test, GravityProperties.adopter)
//            sap.insertBox(hitBox)
//        }

        hitBoxes.updateModelMatrix()
        sap.sort()

        gameState = RenderCategory.FirstPerson
        window.setCursorVisible(false)
    }

    private val mainMenu = MainMenuPage(startButtonOnClick)
    private val mainGui = MainGuiPage()

    private val sap = SAP()
    private val hitBoxes = HitBoxRenderer()
    private val gravityContainer = GravityObjectContainer()

    //scene setup
    init {

//        repeat(200){
//
//            val hitBox = HitBox(sap.idCounter)
//            hitBox.translateLocal(Vector3f(Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat(),Random.nextInt(1,101).toFloat()))
//            val Test = GravityHitBox(hitBox,Random.nextInt(1,101).toFloat(), Vector3f(0f))
//            hitBox.updateEndPoints()
//            hitBoxes.add(hitBox)
//            gravityContainer.add(Test, GravityProperties.sourceAndAdopter)
//            sap.insertBox(hitBox)
//        }

//        val mainGravityObject = Test(HitBox(sap.idCounter),4000000f)
//        mainGravityObject.hitBox.translateLocal(Vector3f(1000f))
//        mainGravityObject.hitBox.scaleLocal(Vector3f(100f))
//        gravityContainer.add(mainGravityObject, GravityProperties.source)
//        hitBoxes.add(mainGravityObject.hitBox)

//        val hitBox = HitBox(0)
//        hitBox.translateLocal(Vector3f(0f))
//        hitBox.scaleLocal(2f)
//
//        hitBox.updateEndPoints()
//
//        val Test = GravityHitBox(hitBox,2f, Vector3f(0f))
//
//        gravityContainer.add(Test, GravityProperties.source)
//        hitBoxes.add(hitBox)
//
//
//
//        val hitBox2 = HitBox(1)
//        hitBox2.translateLocal(Vector3f(20f,0f,0f))
//        hitBox2.scaleLocal(1f)
//
//        hitBox2.updateEndPoints()
//
//        val Test1 = GravityHitBox(hitBox2,0.1f, Vector3f(0f, 0.81694555f,0f))
//
//        gravityContainer.add(Test1, GravityProperties.adopter)
//        hitBoxes.add(hitBox2)

//            val hitBox3 = HitBox(2)
//            hitBox3.translateLocal(Vector3f(-100f))
//            hitBox3.scaleLocal(50f)

//            hitBox3.updateEndPoints()

//            val Test2 = GravityHitBox(hitBox3,100f, Vector3f(0f))

//            gravityContainer.add(Test2, GravityProperties.source)
//            hitBoxes.add(hitBox3)

            hitBoxes.updateModelMatrix()
            sap.sort()



        /*
        repeat(2000){
            val x1 = Random.nextInt(0,200).toFloat()
            val y1 = Random.nextInt(0,200).toFloat()
            val z1 = Random.nextInt(0,200).toFloat()

            val hitBox = HitBox(sap.idCounter)
            hitBox.translateLocal(Vector3f(x1,y1,z1))

            hitBoxes.add(hitBox)
         }

         hitBoxes.hitboxes.forEach {
             sap.insertBox(it)
         }

         sap.sort()

         println( measureTimeMillis { sap.checkCollision() })


         var bestJobSize = 0
         var bestJobTime = Long.MAX_VALUE

         repeat(100){
             val time = measureTimeMillis {sap.checkCollisionHalfParallel(it + 1)}
             if(bestJobTime > time){
                 bestJobTime = time
                 bestJobSize = it
             }

         }
         println("1 job[${bestJobSize + 1}]: $bestJobTime")

        bestJobSize = 0
        bestJobTime = Long.MAX_VALUE

         repeat(100){
             val time = measureTimeMillis {sap.checkCollisionParallel(it + 1)}
             if(bestJobTime > time){
                 bestJobTime = time
                 bestJobSize = it
             }
         }
         println("2 job[${bestJobSize + 1}]: $bestJobTime")



        bestJobSize = 0
        bestJobTime = measureNanoTime {hitBoxes.updateModelMatrix()}
        repeat(100){
            val time = measureNanoTime {hitBoxes.updateModelMatrixParallel(it+1)}
            if(bestJobTime > time){
                bestJobTime = time
                bestJobSize = it
            }
        }
        println("updateModelMatrix[${bestJobSize + 1}]: $bestJobTime")
        */

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
         //window.setCursorVisible(false)

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


//        SceneStats.setWindowCursor(SystemCursor.Arrow)

//        testGuiElement.globalOnUpdateEvent(dt, t)

        if(gameState == RenderCategory.FirstPerson){

//            println("applyGravityFromAll: ${measureNanoTime {
//                gravityContainer.applyGravityParallel(10)
//            }}")
//            println("sort: ${measureNanoTime {
//                sap.sortParallel()
//            }}")
//            println("checkCollision: ${measureNanoTime {
//                sap.checkCollisionParallel(10)
//            }}")
//            println("updateModelMatrix: ${measureNanoTime {
//                hitBoxes.updateModelMatrix()
//            }}")
            if(mainMenu.executeParallel){
                gravityContainer.applyGravityParallel(4)
                hitBoxes.updateModelMatrix()
                sap.sortParallel()
                sap.checkCollisionParallel(4)

            }else{
                gravityContainer.applyGravity()
                hitBoxes.updateModelMatrix()
                sap.sort()
                sap.checkCollision()
            }

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

//        when(speedMarker.state){
//            1 -> solarSystem.update(dt,t)
//            2 -> {
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//            }
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

    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun updateUI(dt: Float, t: Float) {
        if(gameState == RenderCategory.Gui)
            mainMenu.globalOnUpdateEvent(dt, t)

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



        if (gameState == RenderCategory.FirstPerson){
            if (window.getKeyState ( GLFW_KEY_T))
                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 15))

            if (window.getKeyState ( GLFW_KEY_A))
                movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)

            if (window.getKeyState ( GLFW_KEY_D))
                movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
        }
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

        SceneStats.mouseScroll = 0
    }

    private var lastCameraMode = gameState
    private var lastCamera = camera

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        //println("key:$key scancode:$scancode action:$action mode:$mode")

//        if(action == 1 || action == 2)
//            testGuiElement.focusedElement?.onKeyDown?.let { it(key, scancode, mode) }

        if(gameState == RenderCategory.Gui && (action == 1 || action == 2))
            mainMenu.focusedElement?.onKeyDown?.let { it(key, scancode, mode) }



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
//            testGuiElement.globalClickEvent(button, action, Vector2f(mouseXPos, mouseYPos))
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


        // sets cursor position Vector2f(openGlMouseXPos / openGlMouseYPos)
//        cursorGuiElement.setPosition(Vector2f(((mouseXPos / window.windowWidth) * 2) -1,((mouseYPos / window.windowHeight) * 2) -1))
//        println("MouseXPos: [$mouseXPos] $xpos | MouseYPos: [$mouseYPos]")



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


//        testGuiElement.cleanup()
//        fpsGuiElement.cleanup()

        mainMenu.cleanup()
        mainGui.cleanup()


        guiRenderer.cleanup()
        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()
    }




}
