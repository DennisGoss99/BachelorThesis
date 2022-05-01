package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources

class ToggleButton(
    var value: Boolean = false,
    widthConstraint: IScaleConstraint,
    heightConstraint: IScaleConstraint,
    translateXConstraint: ITranslateConstraint,
    translateYConstraint: ITranslateConstraint,
    rounded : Boolean = false
)
    : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, StaticResources.componentColor, 0){


    init {
        children = listOf(
            Box(Relative(0.4f), Relative(1f), PixelLeft(0), Center() , color = StaticResources.componentColor2, cornerRadius)
        )



        statusChanged(value)
    }

    private fun statusChanged(v : Boolean){
        if(value){
            children[0].translateXConstraint = PixelRight(0)
            children[0].color = StaticResources.activeColor
            color = StaticResources.active2Color
        }else{
            children[0].translateXConstraint = PixelLeft(0)
            children[0].color = StaticResources.componentColor2
            color = StaticResources.componentColor
        }
        refresh()
    }

    override var onClick: ((Int, Int) -> Unit)? = {
        _,_ ->
        value = !value
        statusChanged(value)

    }

    override val onUpdate: ((dt: Float, t: Float) -> Unit) = {
            dt: Float, t: Float ->
        checkOnHover()

        if(isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)
    }



}

