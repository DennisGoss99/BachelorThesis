package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class Scrollbar (widthConstraint : IScaleConstraint,
                 heightConstraint : IScaleConstraint,
                 translateXConstraint : ITranslateConstraint,
                 translateYConstraint : ITranslateConstraint,
                 override var color: Vector4f = Color.red,
                 var cornerRadius : Int = 0,
                 public override var onClick: ((Int, Int) -> Unit)? = null,
                 override val onFocus: (() -> Unit)? = null,
                 var innerElement : GuiElement) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, listOf()){

    private var sliderPercentage : Float = 0f

    private val sliderKnob = Box(PixelWidth(20), Relative(0.5f), PixelRight(0), PixelTop(0))
    private val slider = listOf(
        Box(PixelWidth(20), Relative(1f), PixelRight(0), Center(), Color.grey),
        sliderKnob
    )

    init {
        this.children = listOf(innerElement).plus(slider)
        sliderKnob.onClick = {_,_->}
    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
            dt: Float, t: Float ->
        sliderKnob.checkOnHover()
        sliderKnob.checkPressed()

        if(sliderKnob.isPressed){
            sliderPercentage = ((-SceneStats.mousePosition.y - (sliderKnob.getHeight() * 0.5f) + getWorldPixelPosition().y) / (getHeight() - sliderKnob.getHeight())).coerceIn(0f,1f)
            this.refresh()
        }

        sliderKnob.color = if(sliderKnob.isPressed || sliderKnob.isHovering)
            StaticResources.highlightColor
        else
            Color.withe
    }

    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("limitRenderArea",1)
        shaderProgram.setUniform("elementCorners", getWorldPixelPosition())
        super.bind(shaderProgram)
    }

    override fun afterChildrenRender(shaderProgram: ShaderProgram) {
        super.afterRender(shaderProgram)
        shaderProgram.setUniform("limitRenderArea",0)
    }

    override fun refresh() {
        val sliderNobMaxMovement = this.getHeight() - sliderKnob.getHeight()
        sliderKnob.translateYConstraint = PixelTop((sliderNobMaxMovement * sliderPercentage).toInt())
        innerElement.translateYConstraint = PixelTop(-(innerElement.getHeight() / 2f * sliderPercentage).toInt())

        super.refresh()
    }

}