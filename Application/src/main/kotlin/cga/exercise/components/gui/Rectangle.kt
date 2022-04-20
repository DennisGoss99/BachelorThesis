package cga.exercise.components.gui

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30

class Rectangle(private var scale: Vector2f = Vector2f(1f),
                private var translate: Vector2f = Vector2f(0f),
                private var roll: Float = 0f,
                override var color : Vector4f = Vector4f(1f,1f,1f,1f),
                children: List<GuiElement> = listOf()) : GuiElement(children) {

    init {

        translateLocal(translate)
        rotateLocal(roll)
        scaleLocal(scale)
    }

    override fun render(shaderProgram: ShaderProgram) {

    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
    }

    override fun cleanup() {
        super.cleanup()
    }
}