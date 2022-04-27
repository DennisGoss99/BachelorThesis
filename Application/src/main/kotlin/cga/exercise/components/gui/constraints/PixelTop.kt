package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats

class PixelTop(pixel : Int) : ITranslateConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = WindowStats.windowHeight

    override fun getTranslate(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.y ?: 1f
        return -guiElement.heightConstraint.getScale(guiElement) + 1f - (pixel / windowPixel.toFloat() * 2f * (1/parentScale))
    }
}