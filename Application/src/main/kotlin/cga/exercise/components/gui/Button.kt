package cga.exercise.components.gui

import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.text.FontType
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class Button (text : String,
              widthConstraint : IScaleConstraint,
              heightConstraint : IScaleConstraint,
              translateXConstraint : ITranslateConstraint,
              translateYConstraint : ITranslateConstraint,
              override var color: Vector4f = StaticResources.componentColor2,
              cornerRadius : Int = 0,
              override var onClick: ((Int, Int) -> Unit)? = null,
              fontType: FontType = StaticResources.standardFont) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius)
{
    init {
        children = listOf(
            Box(Relative(0.984375f),Relative(0.96875f), PixelLeft(0), PixelTop(0) , color = StaticResources.componentColor, cornerRadius),
            Text(text,5f, fontType, 10f, TextMode.Center, true, Center(), Center(), color = StaticResources.fontColor))
    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
        dt: Float, t: Float ->
        checkOnHover()
        checkPressed()

        if(isPressed){
            children[0].color = StaticResources.componentColor3
        }else
            children[0].color = if(isHovering)
                StaticResources.highlightColor
            else
                StaticResources.componentColor

        if(isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)
    }


}