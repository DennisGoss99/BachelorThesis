package cga.exercise.components.gui

import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.IScaleConstraint
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class NumberBox(text : String,
                widthConstraint : IScaleConstraint,
                heightConstraint : IScaleConstraint,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                backgroundColor: Color = StaticResources.componentColor,
                fontColor: Color = StaticResources.fontColor,
                textMode: TextMode = TextMode.Center,
                multiLine : Boolean = false,
                cornerRadius : Int = 0,
                fontSize : Float = 4f,
                fontType: FontType = StaticResources.standardFont,
                cursorColor : Color = Color(20,20,20),
                OnValueChanged : ((String) -> Unit)? = null) : TextBox(text, widthConstraint, heightConstraint, translateXConstraint, translateYConstraint, backgroundColor, fontColor, textMode, multiLine, cornerRadius, fontSize, fontType, cursorColor, OnValueChanged) {

    override var text : String = ""
        set(value) {
            val valueWithNumbers = value.fold(""){acc, c -> if (c.isDigit()) acc + c else acc }

            field = valueWithNumbers
            (children.getOrNull(0) as Text?)?.text = valueWithNumbers
        }
        get() = (children[0] as Text).text


    override val onKeyDown: (Int, Int, Int) -> Unit = { key: Int, scancode: Int, mode: Int ->
        if(StaticResources.keyIsDigit(key, mode) || StaticResources.keyIsInstruction(key, mode))
            super.onKeyDown(key, scancode, mode)
    }

    override val onLossFocus: (() -> Unit) = {
        if(this.text.isBlank())
            this.text = "0"
    }

}