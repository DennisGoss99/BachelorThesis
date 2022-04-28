package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class Button (text : String,
              widthConstraint : IScaleConstraint,
              heightConstraint : IScaleConstraint,
              translateXConstraint : ITranslateConstraint,
              translateYConstraint : ITranslateConstraint,
              color: Vector4f = Color(30,30,30),
              cornerRadius : Int = 0,
              override val onClick: ((Int, Int) -> Unit)? = null,
              fontType: FontType = StaticResources.standardFont) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius)
{
    init {
        children = listOf(
            Box(Relative(0.984375f),Relative(0.96875f), PixelLeft(0), PixelTop(0) , color = Color(170,170,170), cornerRadius),
            Text(text,5f, fontType, 10f, TextMode.Center, true, Center(), Center(), color = Color(20,20,20)))
    }

}