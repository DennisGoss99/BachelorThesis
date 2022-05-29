package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.joml.Vector4f

class Image (var texture: Texture2D,
             widthConstraint : IScaleConstraint,
             heightConstraint : IScaleConstraint,
             translateXConstraint : ITranslateConstraint,
             translateYConstraint : ITranslateConstraint,
             color : Color = Color(0,0,0,0),
             cornerRadius : Int = 0,
             children: List<GuiElement> = listOf()) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius, children = children) {

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
        shaderProgram.setUniform("useImage" , 1)
        shaderProgram.setUniform("texture2D", 0)
        if (cornerRadius != 0)
            shaderProgram.setUniform("cornerRadius", cornerRadius)
        texture.bind(0)
    }

    override fun afterRender(shaderProgram: ShaderProgram){
        shaderProgram.setUniform("useImage" , 0)
        if (cornerRadius != 0)
            shaderProgram.setUniform("cornerRadius", 0)
    }

    override fun cleanup() {
        super.cleanup()
        texture.cleanup()
    }

    override fun disable(b: Boolean) {
        isDisabled = b
        children.forEach { it.disable(b) }
    }

}

