package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement

class Relative(private val p : Float) : IScaleConstraint, ITranslateConstraint, Constraint() {

    override fun getTranslate(guiElement: GuiElement): Float = p
    override fun getScale(guiElement: GuiElement): Float = p

}