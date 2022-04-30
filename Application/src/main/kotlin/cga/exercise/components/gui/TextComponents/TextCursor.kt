package cga.exercise.components.gui.TextComponents

import cga.exercise.components.gui.Box
import cga.exercise.components.gui.EditText
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector4f

class TextCursor (widthConstraint : IScaleConstraint,
                  heightConstraint : IScaleConstraint,
                  translateXConstraint : ITranslateConstraint,
                  translateYConstraint : ITranslateConstraint,
                  override var color : Vector4f = Vector4f(0f,0f,0f,0f)) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color) {

    val offsetX = 0.004f

    var lastRender = 0f


    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
        dt, t ->

        if(!hasFocus || (t - lastRender).toInt() > 0.5)
            color.w = 0f
        else
            color.w = 1f

        if((t - lastRender).toInt() > 1)
            lastRender = t
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
        shaderProgram.setUniform("transformationMatrix" , getLocalModelMatrix(),false)
    }

    override fun refresh() {
        super.refresh()

        val parent = parent as EditText

        val mat = parent.parent?.getWorldModelMatrix() ?: Matrix4f()

        mat.translate(parent.translateXConstraint.getTranslate(parent), parent.translateYConstraint.getTranslate(parent),0f)

        val translateColumn = Vector4f()
        mat.getColumn(3, translateColumn)

        translateColumn.x += parent.realCursorX * 2f + offsetX
        translateColumn.x -= parent.cursorLineLength

        translateColumn.y += parent.realCursorY

        mat.setColumn(3, translateColumn)

        mat.set(0,0, widthConstraint.getScale(this))
        mat.set(1,1, heightConstraint.getScale(this))

        modelMatrix = mat

    }
}