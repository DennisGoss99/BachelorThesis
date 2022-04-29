package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.exercise.game.SceneStats

class PixelWidth (pixel : Int) : IScaleConstraint, PixelConstraint(pixel) {

    override val windowPixel: Int
        get() = SceneStats.windowWidth

    override fun getScale(guiElement: GuiElement): Float {
        val parentScale = guiElement.parent?.getWorldScale()?.x ?: 1f
        return pixel / windowPixel.toFloat() * (1/ parentScale)
    }

}