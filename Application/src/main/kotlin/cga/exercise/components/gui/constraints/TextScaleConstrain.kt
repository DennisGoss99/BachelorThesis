package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement

internal class TextScaleConstrain : IScaleConstraint, Constraint(){

    var relativeScale = 0f

    override fun getScale(guiElement: GuiElement): Float {
        return relativeScale
    }

}