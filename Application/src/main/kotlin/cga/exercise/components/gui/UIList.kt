package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative

class UIList(widthConstraint : IScaleConstraint,
             heightConstraint : IScaleConstraint,
             translateXConstraint : ITranslateConstraint,
             translateYConstraint : ITranslateConstraint,
             children: List<GuiElement> = listOf()) : LayoutBox(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, children){

    private val translateYConstraintChildrenList = children.map { it.translateYConstraint }

    init {

    }

    override fun refresh() {
        var spaceToNextObject = 1f

        super.refresh()
        children.forEachIndexed { index, child ->
            spaceToNextObject += translateYConstraintChildrenList[index].getTranslate(child) -1
            child.translateYConstraint = Relative(spaceToNextObject)
            spaceToNextObject -= child.getHeight()
        }

        super.refresh()
    }

}