package cga.exercise.components.gui

import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector4f

abstract class GuiElement(var children: List<GuiElement> = listOf(), open var color : Vector4f = Vector4f(1f,1f,1f,1f)) : Transformable2D(){

    init {
        children.forEach { it.parent = this }
    }

    abstract fun render(shaderProgram: ShaderProgram)

    open fun bind(shaderProgram: ShaderProgram){
        shaderProgram.setUniform("useImage" , 0)
        shaderProgram.setUniform("transformationMatrix" , getWorldModelMatrix(),false)
        shaderProgram.setUniform("elementColor" , color)
    }

    open fun cleanup(){
        children.forEach { it.cleanup() }
    }

}