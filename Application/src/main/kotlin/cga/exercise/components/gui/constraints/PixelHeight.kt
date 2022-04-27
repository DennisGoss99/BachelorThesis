package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats

class PixelHeight (pixel : Int) : IScaleConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = WindowStats.windowHeight

    override fun getScale(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.y ?: 1f
        return (pixel / windowPixel.toFloat()) / parentScale
    }

}