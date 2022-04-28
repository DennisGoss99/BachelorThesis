package cga.exercise.components.gui


import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.gui.TextComponents.TextCursor
import cga.exercise.components.gui.constraints.Center
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import java.util.*

class EditText (text : String,
                fontSize : Float,
                font : FontType,
                maxLineLength : Float,
                textMode : TextMode,
                multiline : Boolean = false,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Text(text, fontSize, font, maxLineLength, textMode, multiline, translateXConstraint, translateYConstraint, color) {

    var realCursorX = 0f
        private set

    var realCursorY = 0f
        private set

    var cursorLineLength = 0f

    init {
        children = listOf(
            TextCursor(Relative(0.0005f * fontSize), Relative(0.009f * fontSize), Center(), Center(), color = Color(220,220,220)))
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
    }

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
    }

    fun moveCursorX(amount : Int){

        val newCursorPosX = cursorPosX + amount

        //Move Right
        if(newCursorPosX >= 0) {
            //Move on Line
            if(newCursorPosX <= textChars[cursorPosY].size) {
                cursorPosX = newCursorPosX

                // Move Right Down
            }else{
                if(cursorPosY < textChars.size -1)
                {
                    cursorPosX = 0
                    cursorPosY++
                }
            }
        }else{
            //Move Left Up
            if(cursorPosY > 0){
                cursorPosY--
                cursorPosX = textChars[cursorPosY].size + newCursorPosX + 1
            }

        }
        refreshCursor()
    }

    fun moveCursorY(amount : Int){

        val newCursorPosY = cursorPosY + amount

        if(newCursorPosY >= 0 && newCursorPosY < textChars.count()) {
            cursorPosY = newCursorPosY

            if(cursorPosX > textChars[cursorPosY].size){
                cursorPosX = textChars[cursorPosY].size
            }

            refreshCursor()
        }
    }

    fun setCursorStart() {
        cursorPosX = 0
        cursorX = 0f
        realCursorX = 0f

        refreshCursor()
    }

    fun setCursorEnd() {
        cursorPosX = textChars[cursorPosY].count()
        cursorX = textChars[cursorPosY].fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
        realCursorX = cursorX

        refreshCursor()
    }

    private fun refreshCursor(){
        when(textMode){
            TextMode.Center -> {
                realCursorX = textChars[cursorPosY].subList(0,cursorPosX).fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
                realCursorY = lineHeight * ((textChars.size-1) - cursorPosY * 2f)
                cursorLineLength = textChars[cursorPosY].fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3f }
            }
            TextMode.Left -> {
                realCursorX = textChars[cursorPosY].subList(0,cursorPosX).fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
                realCursorY = lineHeight * ((textChars.size-1) - cursorPosY * 2f)
                cursorLineLength = getMaxLength()
            }
            TextMode.Right -> {
                realCursorX = -textChars[cursorPosY].subList(cursorPosX,textChars[cursorPosY].size).fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
                realCursorY = lineHeight * ((textChars.size-1) - cursorPosY * 2f)
                cursorLineLength = -getMaxLength()//textChars[cursorPosY].fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3f }
            }
        }
    }

    override fun textHasChanged() {
        super.textHasChanged()
        refreshCursor()
    }

}