package cga.exercise.components.gui


import cga.exercise.components.gui.TextComponents.TextCursor
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector4f

class EditText (text : String,
                fontSize : Float,
                font : FontType,
                maxLineLength : Float,
                centeredX : Boolean,
                centeredY : Boolean,
                translate : Vector2f = Vector2f(0f,0f),
                color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Text(text, fontSize, font, maxLineLength, centeredX, centeredY, translate, color) {

    init {
        children = listOf(
            TextCursor(Vector2f(0.0005f * fontSize,0.009f * fontSize), color = Color(220,220,220))
        )
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
    }

}