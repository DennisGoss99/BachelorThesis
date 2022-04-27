package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement

interface IScaleConstraint {

    fun getScale(guiElement: GuiElement) : Float

}