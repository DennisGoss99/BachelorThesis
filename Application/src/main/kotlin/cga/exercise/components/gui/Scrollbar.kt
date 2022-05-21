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
                 private var verticalScrollBarVisibility : Boolean,
                 private var horizontalScrollBarVisibility : Boolean,
                 override var color: Vector4f = Color.red,
                 var cornerRadius : Int = 0,
                 public override var onClick: ((Int, Int) -> Unit)? = null,
                 override val onFocus: (() -> Unit)? = null,
                 var innerElement : GuiElement) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, listOf()){

    private var verticalSliderPercentage : Float = 0f
        set(value) { field = value.coerceIn(0f, 1f)}

    private val verticalSliderKnob = Box(PixelWidth(20), Relative(0.5f), PixelRight(0), PixelTop(0))
    private val verticalSlider = Box(PixelWidth(20), Relative(1f), PixelRight(0), Center(), Color.grey)

    private var horizontalSliderPercentage : Float = 0f
        set(value) { field = value.coerceIn(0f, 1f)}

    private val horizontalSliderKnob = Box(Relative(0.5f), PixelHeight(20), Center(), PixelBottom(0))
    private val horizontalSlider = Box(Relative(1f), PixelHeight(20), Center(), PixelBottom(0), Color.grey)

    init {

        val tempChildren = mutableListOf(innerElement)

        if(horizontalScrollBarVisibility){
            tempChildren.add(horizontalSlider)
            tempChildren.add(horizontalSliderKnob)

            horizontalSliderKnob.onClick = {_,_->}
        }
        if(verticalScrollBarVisibility){
            tempChildren.add(verticalSlider)
            tempChildren.add(verticalSliderKnob)

            verticalSliderKnob.onClick = {_,_->}
        }

        this.children = tempChildren
    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
            dt: Float, t: Float ->

        if(verticalScrollBarVisibility){
            this.checkOnHoverOrChildHover()
            verticalSliderKnob.checkOnHover()
            verticalSliderKnob.checkPressed()


            if(verticalSliderKnob.isPressed){
                verticalSliderPercentage = ((-SceneStats.mousePosition.y - (verticalSliderKnob.getHeight() * 0.5f) + getWorldPixelPosition().y) / (getHeight() - verticalSliderKnob.getHeight()))

                val sliderNobMaxMovement = this.getHeight() - verticalSliderKnob.getHeight()
                verticalSliderKnob.translateYConstraint = PixelTop((sliderNobMaxMovement * verticalSliderPercentage).toInt())
                innerElement.translateYConstraint = PixelTop(-(innerElement.getHeight() / 2f * verticalSliderPercentage).toInt())

                refresh()
            }

            verticalSliderKnob.color = if(verticalSliderKnob.isPressed || verticalSliderKnob.isHovering)
                StaticResources.highlightColor
            else
                Color.withe


            if(isHovering && SceneStats.mouseScroll != 0){
                verticalSliderPercentage -= SceneStats.mouseScroll * 0.1f

                val sliderNobMaxMovement = this.getHeight() - verticalSliderKnob.getHeight()
                verticalSliderKnob.translateYConstraint = PixelTop((sliderNobMaxMovement * verticalSliderPercentage).toInt())
                innerElement.translateYConstraint = PixelTop(-(innerElement.getHeight() / 2f * verticalSliderPercentage).toInt())

                refresh()
            }
        }

        if(horizontalScrollBarVisibility){
            horizontalSliderKnob.checkOnHover()
            horizontalSliderKnob.checkPressed()

            if(horizontalSliderKnob.isPressed){
                horizontalSliderPercentage = ((SceneStats.mousePosition.x - (horizontalSliderKnob.getWidth() * 0.5f) - getWorldPixelPosition().x) / (getWidth() - horizontalSliderKnob.getWidth()))


                val sliderNobMaxMovement = this.getWidth() - horizontalSliderKnob.getWidth()
                horizontalSliderKnob.translateXConstraint = PixelLeft((sliderNobMaxMovement * horizontalSliderPercentage).toInt())
                innerElement.translateXConstraint = PixelLeft(-(innerElement.getWidth() / 2f * horizontalSliderPercentage).toInt())

                refresh()
            }

            horizontalSliderKnob.color = if(horizontalSliderKnob.isPressed || horizontalSliderKnob.isHovering)
                StaticResources.highlightColor
            else
                Color.withe
        }

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

}