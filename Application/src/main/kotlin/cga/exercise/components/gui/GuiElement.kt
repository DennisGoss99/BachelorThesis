package cga.exercise.components.gui

import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import org.joml.Vector2f
import org.joml.Vector4f

abstract class GuiElement(children: List<GuiElement>) : Transformable2D() {

    private var _focusedElement : GuiElement? = null

    var focusedElement : GuiElement?
    get() = getMasterParent()._focusedElement
    set(value) {
        getMasterParent()._focusedElement = value
    }

    var hasFocus = false

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
    protected open val onFocus : (() -> Unit)? = null
    open val onKeyDown : ((Int, Int, Int) -> Unit)? = null

    private fun getMasterParent() : GuiElement{
        return parent?.getMasterParent() ?: this
    }

    fun globalClickEvent(button: Int, mode: Int, position: Vector2f) : Boolean{
        val elementPosition = getWorldPixelPosition()

        hasFocus = false
        var childrenFocus = false
        children.forEach {
            if(it.globalClickEvent(button, mode, position))
                childrenFocus = true
        }

        if(elementPosition.x <= position.x && elementPosition.z >= position.x && elementPosition.y >= position.y && elementPosition.w <= position.y){

            if(!childrenFocus){
                when{
                    onFocus == null && onClick == null -> return false
                    onFocus != null -> {
                        focusedElement = this
                        hasFocus = true
                        onFocus?.let { it() }
                    }
                    onClick != null ->{
                        onClick?.let { it(button, mode) }
                    }
                }
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