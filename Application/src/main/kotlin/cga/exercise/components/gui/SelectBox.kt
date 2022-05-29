package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.components.texture.Texture2D
import cga.exercise.game.StaticResources
import org.joml.Vector4f

class SelectBox<T>(
    initValue : T,
    val values : List<T>,
    widthConstraint : IScaleConstraint,
    heightConstraint : IScaleConstraint,
    translateXConstraint : ITranslateConstraint,
    translateYConstraint : ITranslateConstraint,
    color : Color = StaticResources.componentColor,
    private val itemColor : Color = StaticResources.componentColor2,
    cornerRadius : Int = 0,
    private val OnValueChanged : ((T) -> Unit)? = null,
    private val innerElement : Text,
    private val valueSelector : ((T) -> String) = {it -> it.toString()}
            ) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius) {


    var value = initValue
        set(value) {
            field = value
            refresh()
        }

    override val onFocus: (() -> Unit) = {
        hasFocus = true
        setVisibility(true)
    }

    private val arrowDown = Texture2D("assets/textures/uiSystem/dropdown arrow.png", true)
    private val arrowUp = Texture2D("assets/textures/uiSystem/dropdown arrow up.png", true)

    init {
        setChildren()
    }

    private fun setChildren(){
        val tempChildren = mutableListOf<GuiElement>()

        values.forEachIndexed { index, it ->

            val box = Box(Relative(1f), Relative(1f), Center(), Relative(-2f * (index + 1)), itemColor, children = listOf(
                Text(valueSelector(it), innerElement.fontSize, innerElement.font, innerElement.maxLineLength, innerElement.textMode, innerElement.multiline, innerElement.translateXConstraint, innerElement.translateYConstraint, innerElement.color)
            ))

            box.onClick = { _, _ ->
                setVisibility(true)
                value = values[index]
                OnValueChanged?.invoke(value)
            }

            box.onUpdate = {_, _ ->
                box.checkOnHover()

                box.color = if(box.isHovering) {
                    MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)
                    StaticResources.highlightColor
                }
                        else
                    itemColor
            }

            tempChildren.add(box)
        }

        children = listOf(
            Text(valueSelector(value), innerElement.fontSize, innerElement.font, innerElement.maxLineLength, innerElement.textMode, innerElement.multiline, innerElement.translateXConstraint, innerElement.translateYConstraint, innerElement.color),
            LayoutBox(Relative(1f), Relative(1f), Center(), Center(), tempChildren),
            Image(arrowDown, PixelWidth(15), PixelHeight(15), PixelRight(15), Center()),
        )

        setVisibility(false)
    }

    private fun setVisibility(b : Boolean){
        children[1].isVisible = b
        children[1].children.forEach { it.isVisible = b }

        (children[2] as Image).texture = if(b) arrowUp else arrowDown
    }

    override fun refresh(){
        setChildren()
        super.refresh()
    }

    override var onUpdate: ((dt: Float, t: Float) -> Unit)? = {
        dt, t ->

        checkOnHover()

        if (isHovering)
            MouseCursor.setWindowCursor(MouseCursor.CursorStyle.Hand)

        if (!hasFocus)
            setVisibility(false)
    }

    override fun cleanup() {
        arrowUp.cleanup()
        arrowDown.cleanup()
        super.cleanup()
    }

}