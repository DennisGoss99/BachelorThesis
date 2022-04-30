package cga.exercise.components.gui

import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.text.FontType
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources
import cga.exercise.game.SystemCursor
import org.joml.Vector4f

class Button (text : String,
              widthConstraint : IScaleConstraint,
              heightConstraint : IScaleConstraint,
              translateXConstraint : ITranslateConstraint,
              translateYConstraint : ITranslateConstraint,
              override var color: Vector4f = Color(30,30,30),
              cornerRadius : Int = 0,
              override var onClick: ((Int, Int) -> Unit)? = null,
              fontType: FontType = StaticResources.standardFont) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius)
{
    init {
        children = listOf(
            Box(Relative(0.984375f),Relative(0.96875f), PixelLeft(0), PixelTop(0) , color = Color(170,170,170), cornerRadius),
            Text(text,5f, fontType, 10f, TextMode.Center, true, Center(), Center(), color = Color(20,20,20)))
    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
        dt: Float, t: Float ->
        checkOnHover()
        checkPressed()

        if(isPressed){
            children[0].color = Color(160,160,160)
        }else
            children[0].color = if(isHovering)
                Color(180,180,190)
            else
                Color(170,170,170)

        if(isHovering)
            SceneStats.setWindowCursor(SystemCursor.Hand)
    }


}