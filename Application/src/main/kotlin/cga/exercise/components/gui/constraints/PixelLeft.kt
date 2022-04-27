package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats

class PixelLeft(pixel : Int) : ITranslateConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = WindowStats.windowWidth

    override fun getTranslate(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.x ?: 1f
        return guiElement.widthConstraint.getScale(guiElement) - 1f + pixel / windowPixel.toFloat() * 2f * (1f / parentScale)
    }


}