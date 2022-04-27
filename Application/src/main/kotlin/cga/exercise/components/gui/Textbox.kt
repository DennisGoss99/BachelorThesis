package cga.exercise.components.gui

import cga.exercise.components.gui.constraints.*
import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import cga.exercise.game.StaticResources.Companion.keyToCharGERLayout
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class Textbox(var text : String,
              widthConstraint : IScaleConstraint,
              heightConstraint : IScaleConstraint,
              translateXConstraint : ITranslateConstraint,
              translateYConstraint : ITranslateConstraint,
              color: Vector4f = Color(180,180,180),
              centered : Boolean = true,
              cornerRadius : Int = 0,
              fontType: FontType = StaticResources.standardFont) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius)
{

    override val onClick: ((Int, Int) -> Unit)? = null

    override val onFocus: (() -> Unit) = {->
        children.forEach { it ->
            it.hasFocus = true
            it.children.forEach { it.hasFocus = true }
        }
    }

    override val onKeyDown : ((Int, Int, Int) -> Unit) = { key: Int, scancode: Int, mode: Int ->
        val textGuiElement = children[0] as EditText

        val keyAsChar = keyToCharGERLayout(key, mode)

        var textHasChanged = false

        if(keyAsChar != null){
            textGuiElement.insertChar(keyAsChar)
            textHasChanged = true
        }else{
            when(key){
                GLFW.GLFW_KEY_BACKSPACE ->{
                    textGuiElement.removeChar()
                    textHasChanged = true
                }
                GLFW.GLFW_KEY_DELETE ->{
                    textGuiElement.removeForwardChar()
                    textHasChanged = true
                }
                GLFW.GLFW_KEY_HOME -> textGuiElement.setCursorStart()
                GLFW.GLFW_KEY_END -> textGuiElement.setCursorEnd()
                GLFW.GLFW_KEY_RIGHT -> textGuiElement.moveCursor(1)
                GLFW.GLFW_KEY_LEFT -> textGuiElement.moveCursor(-1)
            }
        }

        if(textHasChanged) {
            textGuiElement.textHasChanged()
            textGuiElement.refresh()
        }
    }

    init {
        children = listOf(
            EditText(text,4f, fontType, 10f, centered, true, if(centered) Center() else Relative(-1f), Center(), color = Color(20,20,20))
        )
    }

}