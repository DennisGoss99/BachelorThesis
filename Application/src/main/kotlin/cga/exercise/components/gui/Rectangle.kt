package cga.exercise.components.gui

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector2f
import org.joml.Vector4f

open class Rectangle(protected var scale : Vector2f = Vector2f(1f),
                     protected var translate : Vector2f = Vector2f(0f),
                     override var color: Vector4f = Vector4f(1f,1f,1f,1f),
                     protected var cornerRadius : Int = 0,
                     override val onClick: ((Int, Int) -> Unit)? = null,
                     override val onFocus: (() -> Unit)? = null,
                     children: List<GuiElement> = listOf()) : GuiElement(children) {

    init {
        translateLocal(translate)
        scaleLocal(scale)
    }

    override fun render(shaderProgram: ShaderProgram) {

    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
        shaderProgram.setUniform("elementCorners", getWorldPixelPosition())
        shaderProgram.setUniform("borderRadius", cornerRadius)
    }

    override fun cleanup() {
        super.cleanup()
    }

}