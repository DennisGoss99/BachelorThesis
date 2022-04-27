package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats

class PixelWidth (pixel : Int) : IScaleConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = WindowStats.windowWidth

    override fun getScale(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.x ?: 1f
        return pixel / windowPixel.toFloat() * (1/ parentScale)
    }

}