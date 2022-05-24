package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources

class Slider(
    initValue : Float,
    widthConstraint: IScaleConstraint,
    heightConstraint: IScaleConstraint,
    translateXConstraint: ITranslateConstraint,
    translateYConstraint: ITranslateConstraint,
    OnValueChanged : ((Float) -> Unit)? = null
) : LayoutBox(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint) {

    private val lineWidth = 4
    private val sliderKnob = Box(PixelWidth(lineWidth), Relative(1f), Relative(-1f), Center(), StaticResources.componentColor4, cornerRadius = 2)


    var value : Float = 0f
       set (newValue) { field = newValue.coerceIn(0f, 1f); refresh()}

    init {
        children = listOf(
            Box(Relative(1f), PixelHeight(lineWidth), Center(), Center(), StaticResources.componentColor),
            sliderKnob
        )

        value = initValue

        sliderKnob.onClick = {_,_->}
    }

    private var pressLastFrame = false

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
            dt: Float, t: Float ->
        sliderKnob.checkOnHover()
        sliderKnob.checkPressed()

        if(sliderKnob.isPressed){
            value = ((SceneStats.mousePosition.x - getWorldPixelPosition().x - 2) / getPixelWidth())
            sliderKnob.translateXConstraint = Relative(value * 2f - 1f)
            this.refresh()

            pressLastFrame = true
        }

        if (pressLastFrame && !sliderKnob.isPressed){
            OnValueChanged?.invoke(value)
            pressLastFrame = false
        }


        sliderKnob.color = if(sliderKnob.isPressed || sliderKnob.isHovering)
            StaticResources.highlightColor
        else
            StaticResources.componentColor4

        if(sliderKnob.isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)
    }


    override fun refresh() {
        sliderKnob.translateXConstraint = Relative(value * 2f - 1f)
        super.refresh()
    }

}