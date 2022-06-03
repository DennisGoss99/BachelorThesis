package cga.exercise.components.gui

import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources
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
        getMasterParent()._focusedElement?.onLossFocus?.let { it() }
        getMasterParent()._focusedElement = value
    }

    var isVisible = true
    var isDisabled = false
        protected set
    var hasFocus = false
    var isHovering = false
    var isPressed = true

    open var color : Color = StaticResources.backGroundColor
    private var storeColor : Color? = color


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

    open var onClick : ((Int, Int) -> Unit)? = null
    protected open val onFocus : (() -> Unit)? = null
    protected open val onLossFocus  : (() -> Unit)? = null
    open val onHover : (() -> Unit)? = null
    open var onUpdate : ((dt: Float, t: Float) -> Unit)? = null
    open val onKeyDown : ((Int, Int, Int) -> Unit)? = null


    private fun getMasterParent() : GuiElement{
        return parent?.getMasterParent() ?: this
    }

    fun globalOnUpdateEvent(dt: Float, t: Float){
        if (!isVisible || isDisabled)
            return

        onUpdate?.invoke(dt, t)
        children.forEach {
            it.globalOnUpdateEvent(dt, t)
        }
    }

    fun checkOnHover(){
        val elementPosition = getWorldPixelPosition()
        val mousePosition = SceneStats.mousePosition
        isHovering = if(isVisible && elementPosition.x <= mousePosition.x && elementPosition.z >= mousePosition.x && elementPosition.y >= mousePosition.y && elementPosition.w <= mousePosition.y){
            !children.any{ it.isHovering }
        }else
            false
    }

    fun checkOnHoverOrChildHover(){
        val elementPosition = getWorldPixelPosition()
        val mousePosition = SceneStats.mousePosition
        isHovering = isVisible && elementPosition.x <= mousePosition.x && elementPosition.z >= mousePosition.x && elementPosition.y >= mousePosition.y && elementPosition.w <= mousePosition.y
    }

    fun checkPressed(){
        if (!SceneStats.mouseKeyPressed.first)
            isPressed = false
    }

    fun globalClickEvent(button: Int, mode: Int, position: Vector2f) : Boolean{
        val elementPosition = getWorldPixelPosition()

        if(hasFocus){
            focusedElement = null
        }
        hasFocus = false

        var childrenFocus = false
        children.forEach {
            if(it.globalClickEvent(button, mode, position))
                childrenFocus = true
        }

        if(elementPosition.x <= position.x && elementPosition.z >= position.x && elementPosition.y >= position.y && elementPosition.w <= position.y){

            if(!childrenFocus){
                when{
                    !isVisible || isDisabled || (onFocus == null && onClick == null) -> return false
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

    open fun afterChildrenRender(shaderProgram: ShaderProgram) {}

    open fun cleanup(){
        children.forEach { it.cleanup() }
    }

    open fun refresh(){
        clearTransformation()
        translateLocal(translateXConstraint.getTranslate(this), translateYConstraint.getTranslate(this))
        scaleLocal(widthConstraint.getScale(this), heightConstraint.getScale(this))

        children.forEach { it.refresh() }
    }

    open fun updateVariables(){
        refresh()
        children.forEach { it.updateVariables() }
    }

    open fun disable(b : Boolean){
        if(!isDisabled && b)
            storeColor = color

        color = if(b) {
            val c = Color(color).getCopy()
            c.a = 50f
            c
        }
        else
            storeColor ?: color

        isDisabled = b
        children.forEach { it.disable(b) }
    }

}