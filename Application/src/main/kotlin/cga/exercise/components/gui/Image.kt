package cga.exercise.components.gui

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.joml.Vector4f

class Image (private val texture: Texture2D,
             private var scale: Vector2f = Vector2f(1f),
             private var translate: Vector2f = Vector2f(0f),
             override var color : Vector4f = Vector4f(0f,0f,0f,0f),
             children: List<GuiElement> = listOf()) : GuiElement(children) {

    init {

        translateLocal(translate)
        scaleLocal(scale)
    }

    override fun render(shaderProgram: ShaderProgram) {

    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
        shaderProgram.setUniform("useImage" , 1)
        shaderProgram.setUniform("texture2D", 0)
        texture.bind(0)
    }

    override fun cleanup() {
        super.cleanup()
        texture.cleanup()
    }
}

