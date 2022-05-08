package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources

class Slider(
    widthConstraint: IScaleConstraint,
    heightConstraint: IScaleConstraint,
    translateXConstraint: ITranslateConstraint,
    translateYConstraint: ITranslateConstraint
) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint) {

    var sliderPercentage = 0f

    private val lineWidth = 10
    private val sliderKnob = Box(PixelWidth(lineWidth), Relative(1f), Relative(-1f), Center(), StaticResources.componentColor2)

    init {
        children = listOf(
            Box(Relative(1f), PixelHeight(lineWidth), Center(), Center(), StaticResources.componentColor),
            sliderKnob
        )

        sliderKnob.onClick = {_,_->}
    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
            dt: Float, t: Float ->
        sliderKnob.checkOnHover()
        sliderKnob.checkPressed()

        if(sliderKnob.isPressed){
            sliderPercentage = ((SceneStats.mousePosition.x - getWorldPixelPosition().x - 2) / getWidth()).coerceIn(0f,1f)
            sliderKnob.translateXConstraint = Relative(sliderPercentage * 2f - 1f)
            this.refresh()
        }

        sliderKnob.color = if(sliderKnob.isPressed || sliderKnob.isHovering)
            StaticResources.highlightColor
        else
            StaticResources.componentColor2

        if(sliderKnob.isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)
    }

}