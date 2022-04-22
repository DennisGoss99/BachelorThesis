package cga.exercise.components.gui

import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import org.joml.Vector2f
import org.joml.Vector4f

abstract class GuiElement(children: List<GuiElement>) : Transformable2D() {

    open var color : Vector4f = Vector4f(1f,1f,1f,1f)

    var children: List<GuiElement> = listOf()
        set(value) {
            field = value
            children.forEach { it.parent = this }
        }

    init {
        this.children = children
    }

    protected open val onClick : ((Int, Int) -> Unit)? = null

    fun globalClickEvent(button: Int, mode: Int, position: Vector2f) : Boolean{
        val elementPosition = getWorldPixelPosition()

        if(elementPosition.x >= position.x && elementPosition.z <= position.x && elementPosition.y >= position.y && elementPosition.w <= position.y){

            if(!children.any{ it.globalClickEvent(button, mode, position)}){
                if(onClick == null)
                    return false
                else
                    onClick?.let { it(button, mode) }
            }
            return true
        }
        return false
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