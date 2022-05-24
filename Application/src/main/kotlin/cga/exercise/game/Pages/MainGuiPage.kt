package cga.exercise.game.Pages

import cga.exercise.components.gui.*
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class MainGuiPage : GuiElement(Relative(1f), Relative(0.05f), Center(), PixelTop(0)) {

    override var color: Vector4f = Color(40,40,40)

    private val fpsGuiElement : Text = Text("",4f, StaticResources.arial,30f, TextMode.Right,false, PixelRight(5), Center(), color = Color(255f,255f,255f))
    private val countText = Text("",4f, StaticResources.arial,30f, TextMode.Right,false, PixelRight(5), Center(), color = Color(255f,255f,255f))

    init {
        children = listOf(
            LayoutBox(Relative(0.1f), Relative(0.75f), PixelLeft(10),Center(), children = listOf(
                Box(Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30)),
                Text("FPS:",4f, StaticResources.arial,30f, TextMode.Left,false, PixelLeft(5), Center(), color = Color(255f,255f,255f)),
                fpsGuiElement
            )),
            LayoutBox(Relative(0.13f), Relative(0.75f), PixelRight(10),Center(), children = listOf(
                Box(Relative(1f), Relative(1f),Center(),Center(), Color(30,30,30)),
                Text("Anzahl:",4f, StaticResources.arial,30f, TextMode.Left,false, PixelLeft(5), Center(), color = Color(255f,255f,255f)),
                countText
            ))

        )

    }


    fun setValues(objectCount : String){
        countText.text = objectCount
        countText.textHasChanged()
        refresh()
    }

    fun setFPS(FPSCount : String){
        fpsGuiElement.text = FPSCount
        fpsGuiElement.textHasChanged()
        fpsGuiElement.refresh()
    }


}