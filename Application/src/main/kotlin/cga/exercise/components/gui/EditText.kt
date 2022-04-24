package cga.exercise.components.gui


import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector4f

class EditText (text : String,
                fontSize : Float,
                font : FontType,
                maxLineLength : Float,
                centered : Boolean,
                translate : Vector2f = Vector2f(0f,0f),
                color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Text(text, fontSize, font, maxLineLength,centered, translate, color) {

    init {
        children = listOf(
            Rectangle(Vector2f(0.01f,0.7f))
        )
    }

}