package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class LayoutBox (widthConstraint : IScaleConstraint,
                 heightConstraint : IScaleConstraint,
                 translateXConstraint : ITranslateConstraint,
                 translateYConstraint : ITranslateConstraint,
                 children: List<GuiElement> = listOf()) : GuiElement(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, children){

}