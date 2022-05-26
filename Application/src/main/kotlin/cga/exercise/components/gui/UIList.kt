package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative

class UIList(widthConstraint : IScaleConstraint,
             heightConstraint : IScaleConstraint,
             translateXConstraint : ITranslateConstraint,
             translateYConstraint : ITranslateConstraint,
             private val isVertical : Boolean = true,
             children: List<GuiElement> = listOf()) : LayoutBox(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, children){

    private val translateConstraintChildrenList =  children.map { if(isVertical) it.translateYConstraint else it.translateXConstraint }

    override fun refresh() {
        var spaceToNextObject = if (isVertical) 1f else -1f

        super.refresh()
        children.forEachIndexed { index, child ->

            if(isVertical){
                spaceToNextObject += translateConstraintChildrenList[index].getTranslate(child) - 1
                child.translateYConstraint = Relative(spaceToNextObject)
                spaceToNextObject -= child.getHeight()
            }else{
                spaceToNextObject += translateConstraintChildrenList[index].getTranslate(child) + 1
                child.translateXConstraint = Relative(spaceToNextObject)
                spaceToNextObject += child.getWidth()
            }
        }

        super.refresh()
    }

}