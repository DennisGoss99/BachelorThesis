package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement

interface ITranslateConstraint {

    abstract fun getTranslate(guiElement: GuiElement) : Float
}