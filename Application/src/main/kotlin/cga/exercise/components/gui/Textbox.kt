package cga.exercise.components.gui

import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import cga.exercise.game.StaticResources.Companion.keyToCharGERLayout
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class Textbox(var text : String, scale: Vector2f, translate: Vector2f, color: Vector4f = Color(180,180,180), centered : Boolean = true ,cornerRadius : Int = 0, fontType: FontType = StaticResources.standardFont) : Rectangle(scale, translate, color,cornerRadius)
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

        if(keyAsChar != null){
            text += keyAsChar
        }else{
            when(key){
                GLFW.GLFW_KEY_BACKSPACE ->{
                    text = text.dropLast(1)
                }
                GLFW.GLFW_KEY_RIGHT -> {

                }
                GLFW.GLFW_KEY_LEFT -> {

                }
            }
        }


        textGuiElement.text = text
        textGuiElement.textHasChanged()
    }

    init {
        children = listOf(
            EditText(text,5f, fontType, 10f, centered, true, if(centered) Vector2f(0f) else Vector2f(-1f,0f), color = Color(20,20,20))
        )
    }

}