package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.exercise.game.SceneStats

class PixelBottom(pixel : Int) : ITranslateConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = SceneStats.windowHeight

    override fun getTranslate(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.y ?: 1f
        return guiElement.heightConstraint.getScale(guiElement) - 1f + pixel / windowPixel.toFloat() * 2f * (1/parentScale)
    }
}