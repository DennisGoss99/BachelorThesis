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
                 override var color : Color = Color.red,
                 var cornerRadius : Int = 0,
                 public override var onClick: ((Int, Int) -> Unit)? = null,
                 override val onFocus: (() -> Unit)? = null,
                 var innerElement : GuiElement) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, listOf()){

    private var verticalSliderPercentage : Float = 0f
        set(value) { field = value.coerceIn(0f, 1f)}

    private val verticalSliderKnob = Box(PixelWidth(10), Relative(1/innerElement.heightConstraint.getScale(innerElement)), PixelRight(2), PixelTop(0), StaticResources.componentColor4, 5)
    private val verticalSlider = Box(PixelWidth(14), Relative(1f), PixelRight(0), Center(), Color(20,20,20,40))

    private var horizontalSliderPercentage : Float = 0f
        set(value) { field = value.coerceIn(0f, 1f)}

    private val horizontalSliderKnob = Box(Relative(1/innerElement.widthConstraint.getScale(innerElement)), PixelHeight(10), PixelLeft(0), PixelBottom(2), StaticResources.componentColor, 5)
    private val horizontalSlider = Box(Relative(1f), PixelHeight(14), Center(), PixelBottom(0), Color(20,20,20,40))

    init {
        innerElement.translateYConstraint = PixelTop(0)
        innerElement.translateXConstraint = PixelLeft(0)

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

    override var onUpdate: ((dt: Float, t: Float) -> Unit)? = {
            dt: Float, t: Float ->

        if(verticalScrollBarVisibility){
            this.checkOnHoverOrChildHover()
            verticalSliderKnob.checkOnHover()
            verticalSliderKnob.checkPressed()

            verticalSliderKnob.color = if(verticalSliderKnob.isPressed || verticalSliderKnob.isHovering)
                StaticResources.highlightColor
            else
                StaticResources.componentColor

            if ((isHovering && SceneStats.mouseScroll != 0) || verticalSliderKnob.isPressed) {

                if (verticalSliderKnob.isPressed)
                    verticalSliderPercentage = ((-SceneStats.mousePosition.y - (verticalSliderKnob.getPixelHeight() * 0.5f) + getWorldPixelPosition().y) / (getPixelHeight() - verticalSliderKnob.getPixelHeight()))

                if (isHovering && SceneStats.mouseScroll != 0)
                    verticalSliderPercentage -= SceneStats.mouseScroll * 0.1f


                val sliderNobMaxMovement = this.getPixelHeight() - verticalSliderKnob.getPixelHeight()
                verticalSliderKnob.translateYConstraint = PixelTop((sliderNobMaxMovement * verticalSliderPercentage).toInt())
                innerElement.translateYConstraint = PixelTop(-(innerElement.getPixelHeight() * verticalSliderPercentage * (1f - 1f / innerElement.heightConstraint.getScale(innerElement))).toInt())

                refresh()
            }
        }

        if(horizontalScrollBarVisibility){
            horizontalSliderKnob.checkOnHover()
            horizontalSliderKnob.checkPressed()

            if(horizontalSliderKnob.isPressed){
                horizontalSliderPercentage = ((SceneStats.mousePosition.x - (horizontalSliderKnob.getPixelWidth() * 0.5f) - getWorldPixelPosition().x) / (getPixelWidth() - horizontalSliderKnob.getPixelWidth()))


                val sliderNobMaxMovement = this.getPixelWidth() - horizontalSliderKnob.getPixelWidth()
                horizontalSliderKnob.translateXConstraint = PixelLeft((sliderNobMaxMovement * horizontalSliderPercentage).toInt())
                innerElement.translateXConstraint = PixelLeft(-(innerElement.getPixelWidth() * horizontalSliderPercentage * (1f - 1f / innerElement.widthConstraint.getScale(innerElement))).toInt())

                refresh()
            }

            horizontalSliderKnob.color = if(horizontalSliderKnob.isPressed || horizontalSliderKnob.isHovering)
                StaticResources.highlightColor
            else
                StaticResources.componentColor
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