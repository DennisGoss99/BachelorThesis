package cga.exercise.components.gui

import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.SceneStats
import cga.framework.GameWindow
import org.joml.Vector2f
import org.joml.Vector4f

abstract class GuiElement(var widthConstraint : IScaleConstraint = Relative(1f),
                          var heightConstraint : IScaleConstraint = Relative(1f),
                          var translateXConstraint : ITranslateConstraint = Relative(0f),
                          var translateYConstraint : ITranslateConstraint = Relative(0f),
                          children: List<GuiElement> = listOf()
                          ) : Transformable2D() {



    private var _focusedElement : GuiElement? = null

    var focusedElement : GuiElement?
    get() = getMasterParent()._focusedElement
    set(value) {
        getMasterParent()._focusedElement = value
    }

    var isVisible = true
    var hasFocus = false
    var isHovering = false
    var isPressed = true

    open var color : Vector4f = Vector4f(1f,1f,1f,1f)

    var children: List<GuiElement> = listOf()
        set(value) {
            field = value
            children.forEach {
                it.parent = this
            }
        }

    init {
        this.children = children
    }

    protected open val onClick : ((Int, Int) -> Unit)? = null
    protected open val onFocus : (() -> Unit)? = null
    open val onHover : (() -> Unit)? = null
    open val onUpdate : ((dt: Float, t: Float) -> Unit)? = null
    open val onKeyDown : ((Int, Int, Int) -> Unit)? = null

    private fun getMasterParent() : GuiElement{
        return parent?.getMasterParent() ?: this
    }

    fun globalOnUpdateEvent(dt: Float, t: Float){
        onUpdate?.invoke(dt, t)
        children.forEach {
            it.globalOnUpdateEvent(dt, t)
        }
    }

    fun checkOnHover(){
        val elementPosition = getWorldPixelPosition()
        val mousePosition = SceneStats.mousePosition
        isHovering = if(elementPosition.x <= mousePosition.x && elementPosition.z >= mousePosition.x && elementPosition.y >= mousePosition.y && elementPosition.w <= mousePosition.y){
            !children.any{ it.isHovering }
        }else
            false
    }

    fun checkPressed(){
        if (!SceneStats.mouseKeyPressed.first)
            isPressed = false
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
                        isPressed = true
                    }
                }
            }
            return true
        }
        return false
    }

    open fun bind(shaderProgram: ShaderProgram){
        shaderProgram.setUniform("transformationMatrix", getWorldModelMatrix(),false)
        shaderProgram.setUniform("elementColor", color)
    }

    open fun render(shaderProgram: ShaderProgram){}

    open fun afterRender(shaderProgram: ShaderProgram){}

    open fun cleanup(){
        children.forEach { it.cleanup() }
    }

    open fun refresh(){
        clearTransformation()
        translateLocal(translateXConstraint.getTranslate(this), translateYConstraint.getTranslate(this))
        scaleLocal(widthConstraint.getScale(this), heightConstraint.getScale(this))

        children.forEach { it.refresh() }
    }

}