package cga.exercise.components.gui

import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import cga.exercise.game.StaticResources.Companion.keyToCharGERLayout
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class Textbox(var text : String,
              widthConstraint : IScaleConstraint,
              heightConstraint : IScaleConstraint,
              translateXConstraint : ITranslateConstraint,
              translateYConstraint : ITranslateConstraint,
              color: Vector4f = Color(180,180,180),
              textMode: TextMode = TextMode.Center,
              multiLine : Boolean = true,
              cornerRadius : Int = 0,
              fontType: FontType = StaticResources.standardFont) : Box(widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, color, cornerRadius)
{

    override var onClick: ((Int, Int) -> Unit)? = null

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
        }
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
            GLFW.GLFW_KEY_RIGHT -> textGuiElement.moveCursorX(1)
            GLFW.GLFW_KEY_LEFT -> textGuiElement.moveCursorX(-1)
            GLFW.GLFW_KEY_UP -> textGuiElement.moveCursorY(-1)
            GLFW.GLFW_KEY_DOWN -> textGuiElement.moveCursorY(1)
        }


        if(textHasChanged)
            textGuiElement.textHasChanged()

        textGuiElement.refresh()
    }

    init {
        children = listOf(
            when(textMode){
                TextMode.Center -> EditText(text,4f, fontType, 10f, TextMode.Center, multiLine, Center(), Center(), color = Color(20,20,20))
                TextMode.Left -> EditText(text,4f, fontType, 10f, TextMode.Left, multiLine, PixelLeft(5), Center(), color = Color(20,20,20))
                TextMode.Right -> EditText(text,4f, fontType, 10f, TextMode.Right, multiLine, PixelRight(5), Center(), color = Color(20,20,20))
            }

        )
    }

}