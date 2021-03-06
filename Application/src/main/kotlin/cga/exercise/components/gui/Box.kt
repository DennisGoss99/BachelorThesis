package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.Constraint
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.StaticResources
import org.joml.Vector2f
import org.joml.Vector4f

open class Box (widthConstraint : IScaleConstraint,
                heightConstraint : IScaleConstraint,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                override var color : Color = StaticResources.backGroundColor,
                var cornerRadius : Int = 0,
                public override var onClick: ((Int, Int) -> Unit)? = null,
                override val onFocus: (() -> Unit)? = null,
                children: List<GuiElement> = listOf()) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, children){

    init {
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)


        if (cornerRadius != 0) {
            shaderProgram.setUniform("cornerRadius", cornerRadius)
            shaderProgram.setUniform("roundedElementCorners", getWorldPixelPosition())
        }
    }

    override fun afterRender(shaderProgram: ShaderProgram) {
        if (cornerRadius != 0) {
            shaderProgram.setUniform("cornerRadius", 0)
        }
    }

    override fun cleanup() {
        super.cleanup()
    }

}