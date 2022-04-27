package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.Constraint
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector2f
import org.joml.Vector4f

open class Box (widthConstraint : IScaleConstraint,
                heightConstraint : IScaleConstraint,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                override var color: Vector4f = Vector4f(1f,1f,1f,1f),
                protected var cornerRadius : Int = 0,
                override val onClick: ((Int, Int) -> Unit)? = null,
                override val onFocus: (() -> Unit)? = null,
                children: List<GuiElement> = listOf()) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, children){

    init {
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