package cga.exercise.components.gui.TextComponents

import cga.exercise.components.gui.EditText
import cga.exercise.components.gui.Rectangle
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

class TextCursor (scale: Vector2f = Vector2f(1f),
                  translate: Vector2f = Vector2f(0f),
                  color : Vector4f = Vector4f(0f,0f,0f,0f)) : Rectangle(scale, translate, color) {

    val offsetX = 0.004f

    var lastRender = 0f

    init {

    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)

        val parent = parent as EditText

        val mat = getWorldModelMatrix()

        val translateColumn = Vector4f()
        mat.getColumn(3, translateColumn)

        translateColumn.x += parent.cursorX * 2f + offsetX

        if (parent.centeredX)
            translateColumn.x -= parent.length

        if (!parent.centeredY)
            translateColumn.y += 0.01f * parent.fontSize

        mat.setColumn(3, translateColumn)

        val scale = getScaleLocal()

        mat.set(0,0, scale.x)
        mat.set(1,1, scale.y)

        shaderProgram.setUniform("transformationMatrix" , mat,false)
    }

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
    }
}