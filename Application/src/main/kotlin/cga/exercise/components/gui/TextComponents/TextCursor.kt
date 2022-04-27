package cga.exercise.components.gui.TextComponents

import cga.exercise.components.gui.Box
import cga.exercise.components.gui.EditText
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector2f
import org.joml.Vector4f

class TextCursor (widthConstraint : IScaleConstraint,
                  heightConstraint : IScaleConstraint,
                  translateXConstraint : ITranslateConstraint,
                  translateYConstraint : ITranslateConstraint,
                  color : Vector4f = Vector4f(0f,0f,0f,0f)) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color) {

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