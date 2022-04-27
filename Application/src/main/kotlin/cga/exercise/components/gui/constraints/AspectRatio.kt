package cga.exercise.components.gui.constraints

import cga.exercise.components.gui.GuiElement
import cga.framework.WindowStats
import org.joml.Vector2f

class AspectRatio(private val ratio : Float = 1f) : IScaleConstraint, Constraint() {

    private val windowAspectRatioHeightMultiplier
        get() = WindowStats.windowWidth.toFloat() / WindowStats.windowHeight

    private val windowAspectRatioWidthMultiplier
        get() = WindowStats.windowHeight.toFloat() / WindowStats.windowWidth

    override fun getScale(guiElement: GuiElement): Float {
        return when{
            guiElement.widthConstraint is AspectRatio ->{
                val parentScale = guiElement.parent?.getWorldScale() ?: Vector2f(1f,1f)
                guiElement.heightConstraint.getScale(guiElement) * ratio * windowAspectRatioWidthMultiplier * ((parentScale.y) / (parentScale.x))

            }
            guiElement.heightConstraint is AspectRatio ->{
                val parentScale = guiElement.parent?.getWorldScale() ?: Vector2f(1f,1f)
                guiElement.widthConstraint.getScale(guiElement) * ratio * windowAspectRatioHeightMultiplier * ((parentScale.x) / (parentScale.y))
            }
            else -> throw Exception("Width and height cannot be of type AspectRatio at the same time")
        }
    }

}