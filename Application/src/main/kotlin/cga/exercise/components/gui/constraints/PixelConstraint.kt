package cga.exercise.components.gui.constraints


abstract class PixelConstraint(var pixel : Int) : Constraint()  {

    abstract val windowPixel : Int

}