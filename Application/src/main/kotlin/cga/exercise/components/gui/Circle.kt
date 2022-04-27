package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.AspectRatio
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector4f

class Circle(diameterConstraint : IScaleConstraint,
             translateXConstraint : ITranslateConstraint,
             translateYConstraint : ITranslateConstraint,
             color: Vector4f = Vector4f(1f,1f,1f,1f),
             override val onClick: ((Int, Int) -> Unit)? = null,
             override val onFocus: (() -> Unit)? = null,
             children: List<GuiElement> = listOf()) : Box(diameterConstraint, AspectRatio(), translateXConstraint, translateYConstraint, color, children = children){

    override fun refresh() {
        super.refresh()
        val worldPosition = getWorldPixelPosition()
        val radius = (worldPosition.z - worldPosition.x) /2f
        cornerRadius = radius.toInt()
    }

}