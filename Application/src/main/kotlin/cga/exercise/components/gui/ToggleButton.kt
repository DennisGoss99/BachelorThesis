package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.game.StaticResources
import kotlin.math.roundToInt

class ToggleButton(
    var value: Boolean = false,
    widthConstraint: IScaleConstraint,
    heightConstraint: IScaleConstraint,
    translateXConstraint: ITranslateConstraint,
    translateYConstraint: ITranslateConstraint,
    val rounded : Boolean = false,
    private val OnValueChanged : ((Boolean) -> Unit)? = null
)
    : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, StaticResources.componentColor, 0){

    private var colorBackground : Color = StaticResources.componentColor

    init {
        children = listOf(
            Box(AspectRatio(), Relative(0.9f), PixelLeft(2), Center() , color = StaticResources.componentColor2, cornerRadius)
        )
        statusChanged(value)
    }



    private fun statusChanged(v : Boolean){
        if(value){
            children[0].translateXConstraint = PixelRight(2)
            children[0].color = StaticResources.componentColor4
            color = StaticResources.activeColor
            colorBackground = StaticResources.activeColor
        }else{
            children[0].translateXConstraint = PixelLeft(2)
            children[0].color = StaticResources.componentColor4
            color = StaticResources.activeColor2
            colorBackground = StaticResources.activeColor2
        }
        refresh()
    }

    override var onClick: ((Int, Int) -> Unit)? = {
        _,_ ->
        value = !value
        statusChanged(value)
        OnValueChanged?.invoke(value)
    }

    override var onUpdate: ((dt: Float, t: Float) -> Unit)? = {
            dt: Float, t: Float ->
        checkOnHover()

        if(isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)

        if(isHovering)
            color = colorBackground.getCopy{ it + 20f}
        else
            color = colorBackground
    }

    override fun refresh() {
        super.refresh()
        if(rounded) {
            cornerRadius = (getPixelHeight() / 2).roundToInt()
            (children[0] as Box).cornerRadius = (getPixelHeight() / 2).roundToInt()
        }
    }


}

