package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.Box
import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats

abstract class PixelConstraint(var pixel : Int) : Constraint()  {

    abstract val windowPixel : Int

}