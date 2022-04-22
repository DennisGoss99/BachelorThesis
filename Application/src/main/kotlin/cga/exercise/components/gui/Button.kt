package cga.exercise.components.gui

import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import org.joml.Vector2f
import org.joml.Vector4f

class Button (text : String, scale: Vector2f, translate: Vector2f, color: Vector4f = Vector4f(0.3f,0.3f,0.3f,1f), override val onClick: ((Int, Int) -> Unit)? = null, fontType: FontType = StaticResources.standardFont) : Rectangle(scale, translate, color)
{
    init {
        children = listOf(
            Rectangle( Vector2f(0.984375f,0.96875f), Vector2f(-0.015625f,0.03125f) , color = Vector4f(0.6f,0.6f,0.6f,1f)),
            Text(text,5f, fontType, 10f,true, Vector2f(0f,0f), color = Vector4f(0.05f,0.05f,0.05f,1f)
            ))
    }
}