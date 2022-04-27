package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement

class Center : ITranslateConstraint, Constraint() {

    override fun getTranslate(guiElement: GuiElement) : Float = 0f

}