package cga.exercise.game.Pages

import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class MainGuiPage : GuiElement(Relative(1f), Relative(0.05f), Center(), PixelTop(0)) {


    var objectCount = 0
        set(value) {
            field = value
            countText.text = value.toString()
        }

    var executeParallel = true
        set(value) { field = value ; if (value) parallelImageElement.texture = parallelIcon else parallelImageElement.texture = parallelFalseIcon }

    var renderAnything = true
        set(value) { field = value ; if (value) visibilityImageElement.texture = visibilityIcon else visibilityImageElement.texture = visibilityFalseIcon }

    var renderEarth = true
        set(value) { field = value ; if (value) earthImageElement.texture = earthIcon else earthImageElement.texture = earthFalseIcon }

    var executeCollision = true
        set(value) { field = value ; if (value) collisionImageElement.texture = collisionIcon else collisionImageElement.texture = collisionFalseIcon }

    var executeShatter = true
        set(value) { field = value ; if (value) shatterImageElement.texture = shatterIcon else shatterImageElement.texture = shatterFalseIcon }

    var executeGravity = true
        set(value) { field = value ; if (value) gravityImageElement.texture = gravityIcon else gravityImageElement.texture = gravityFalseIcon }

    override var color : Color = Color(40,40,40)



    val fpsGuiElement : Text = Text("",4f, StaticResources.arial,30f, TextMode.Right,false, PixelRight(5), Center(), color = Color(255f,255f,255f))
    val upsGuiElement : Text = Text("",4f, StaticResources.arial,30f, TextMode.Right,false, PixelRight(5), Center(), color = Color(255f,255f,255f))
    private val countText = Text("",4f, StaticResources.arial,30f, TextMode.Right,false, PixelRight(5), Center(), color = Color(255f,255f,255f))

    private val parallelIcon = Texture2D("assets/textures/gui/parallelIcon.png", true)
    private val parallelFalseIcon = Texture2D("assets/textures/gui/parallelFalseIcon.png", true)
    private val parallelImageElement = Image( parallelIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    private val visibilityIcon = Texture2D("assets/textures/gui/visibilityIcon.png", true)
    private val visibilityFalseIcon = Texture2D("assets/textures/gui/visibilityFalseIcon.png", true)
    private val visibilityImageElement = Image( visibilityIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    private val earthIcon = Texture2D("assets/textures/gui/earthIcon.png", true)
    private val earthFalseIcon = Texture2D("assets/textures/gui/earthFalseIcon.png", true)
    private val earthImageElement = Image( earthIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    private val collisionIcon = Texture2D("assets/textures/gui/collisionIcon.png", true)
    private val collisionFalseIcon = Texture2D("assets/textures/gui/collisionFalseIcon.png", true)
    private val collisionImageElement = Image( collisionIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    private val shatterIcon = Texture2D("assets/textures/gui/shatterIcon.png", true)
    private val shatterFalseIcon = Texture2D("assets/textures/gui/shatterFalseIcon.png", true)
    private val shatterImageElement = Image( shatterIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    private val gravityIcon = Texture2D("assets/textures/gui/gravityIcon.png", true)
    private val gravityFalseIcon = Texture2D("assets/textures/gui/gravityFalseIcon.png", true)
    private val gravityImageElement = Image( gravityIcon,Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30))

    init {
        children = listOf(
            UIList(Relative(1f),Relative(1f), Center(), Center(), false, children = listOf(
                LayoutBox(Relative(0.1f), Relative(0.75f), PixelLeft(10),Center(), children = listOf(
                    Box(Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30)),
                    Text("FPS:",4f, StaticResources.arial,30f, TextMode.Left,false, PixelLeft(5), Center(), color = Color(255f,255f,255f)),
                    fpsGuiElement
                )),
                LayoutBox(Relative(0.085f), Relative(0.75f), PixelLeft(10),Center(), children = listOf(
                    Box(Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30)),
                    Text("UPS:",4f, StaticResources.arial,30f, TextMode.Left,false, PixelLeft(5), Center(), color = Color(255f,255f,255f)),
                    upsGuiElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    parallelImageElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    visibilityImageElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    earthImageElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    collisionImageElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    shatterImageElement
                )),
                LayoutBox(AspectRatio(), Relative(0.75f), PixelLeft(10), Center(), children = listOf(
                    gravityImageElement
                )),
            )),

            LayoutBox(Relative(0.13f), Relative(0.75f), PixelRight(10),Center(), children = listOf(
                Box(Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30)),
                Text("Anzahl:",4f, StaticResources.arial,30f, TextMode.Left,false, PixelLeft(5), Center(), color = Color(255f,255f,255f)),
                countText
            ))
        )
    }
}