package cga.exercise.components.gui

import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import org.joml.Vector2f
import org.joml.Vector4f

class Button (text : String, scale: Vector2f, translate: Vector2f, color: Vector4f = Color(30,30,30), cornerRadius : Int = 0, override val onClick: ((Int, Int) -> Unit)? = null, fontType: FontType = StaticResources.standardFont) : Rectangle(scale, translate, color,cornerRadius)
{
    init {
        children = listOf(
            Rectangle( Vector2f(0.984375f,0.96875f), Vector2f(-0.015625f,0.03125f) , color = Color(170,170,170),cornerRadius),
            Text(text,5f, fontType, 10f,true,true, Vector2f(0f,0f), color = Color(20,20,20)
            ))
    }
}