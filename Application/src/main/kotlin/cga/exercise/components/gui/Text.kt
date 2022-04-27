package cga.exercise.components.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.gui.oldGuiElement
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.gui.TextComponents.TextCursor
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import cga.framework.WindowStats
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.util.*

open class Text(text : String,
                var fontSize : Float,
                private val font : FontType,
                val maxLineLength : Float,
                var textMode : TextMode,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                override var color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : GuiElement(TextScaleConstrain(),TextScaleConstrain(), translateXConstraint, translateYConstraint, children = listOf()) {


    private var mesh : SimpleMesh? = null

    var text
        get() = textChars.fold(""){acc, char -> acc + char.id.toChar() }
        set(value) {
            textChars.clear()
            value.forEach { c ->
                textChars.add(convertChar(c))
            }
        }

    var cursorX = 0f
        private set
    var cursorY = 0f
        private set
    var cursorPosX = 0
        private set
    var cursorPosY = 0
        private set

    private val textChars = mutableListOf<cga.exercise.components.text.Char>()

    private var vertexData = mutableListOf<Float>()

    var length = 0f

    private var height = 0f
    var lineHeight = 0f

    private val vao = arrayOf(
        VertexAttribute(2, GL11.GL_FLOAT, 16, 0),
        VertexAttribute(2, GL11.GL_FLOAT, 16, 8)
    )

    init {

        text.forEach { c ->
            textChars.add(convertChar(c))
        }

//        cursorPosX = text.length
        textHasChanged()

    }

    private fun convertChar(character: Char) : cga.exercise.components.text.Char{
        return font.chars[character.code] ?: throw Exception("Character couldn't be found in $font: [$character]")
    }

    private fun getMaxLength() : Float{
        var tempLength = 0f
        var tempLength1 = 0f

        textChars.forEach {
            if(it.id == 10){
                if(tempLength1 > tempLength)
                    tempLength = tempLength1
                tempLength1 = 0f
            }else{
                tempLength1 += it.xAdvance * fontSize / 3
            }
        }

        if(tempLength1 > tempLength)
            tempLength = tempLength1

        return tempLength
    }

    private fun setLetter(fontTypeChar: cga.exercise.components.text.Char) {

        val x = cursorX + fontTypeChar.xOffset * fontSize / 3f
        val y = cursorY + fontTypeChar.yOffset * fontSize / 3f
        val maxX = x + fontTypeChar.sizeX * fontSize / 3f
        val maxY = y + fontTypeChar.sizeY * fontSize / 3f
        val properX = (2 * x) - 1
        val properY = (-2 * y) + 1
        val properMaxX = (2 * maxX) - 1
        val properMaxY = (-2 * maxY) + 1

        addVertices(
            properX,
            properY,
            properMaxX,
            properMaxY,
            fontTypeChar.xTextureCoord,
            fontTypeChar.yTextureCoord,
            fontTypeChar.xMaxTextureCoord,
            fontTypeChar.yMaxTextureCoord
        )

        textChars.add(fontTypeChar)



        if(fontTypeChar.id == 10){
            cursorX = 0f
            cursorY += lineHeight
            height += lineHeight
            cursorPosY++
        }else{
            cursorX += fontTypeChar.xAdvance * fontSize / 3f
        }
    }

    private fun addVertices(x: Float,y: Float,maxX: Float,maxY: Float,texx: Float, texy: Float, texmaxX: Float, texmaxY: Float) {
        Collections.addAll(
            vertexData,
            x, y, texx, texy,
            x, maxY, texx, texmaxY,
            maxX, y, texmaxX, texy,
            maxX, y, texmaxX, texy,
            x, maxY, texx, texmaxY,
            maxX, maxY, texmaxX, texmaxY,
        )
    }

    fun textHasChanged(){
        mesh?.cleanupMesh()

        vertexData.clear()

        lineHeight = 0.01f * fontSize
        height = lineHeight

        //get first line length
        var lineLength = textChars.takeWhile { it.id != 10 }.fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3f }
        //get max line length
        length = getMaxLength()

        //set first xPos starting point
        cursorX = when(textMode){
            TextMode.Center -> (length - lineLength) / 2f
            TextMode.Left -> 0f
            TextMode.Right -> length - lineLength
        }
        cursorY = 0f
        cursorPosY = 0

        val cpyTextChars = textChars.toList()
        textChars.clear()

        cpyTextChars.forEachIndexed { index, it ->
            setLetter(it)

            //set xPos starting point
            if (it.id == 10 && index < cpyTextChars.count()) {
                lineLength = cpyTextChars.drop(index + 1).takeWhile { it.id != 10 }.fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3f }
                cursorX = when(textMode){
                    TextMode.Center -> (length - lineLength) / 2f
                    TextMode.Left -> 0f
                    TextMode.Right -> length - lineLength
                }
            }

        }

        moveCursor(0)

        mesh = SimpleMesh(vertexData.toFloatArray(), vao, font.fontImageMaterial)
    }

    fun moveCursor(amount : Int){

        val cursorPosXNewValue = cursorPosX + amount

        if(cursorPosXNewValue >= 0 && cursorPosXNewValue <= textChars.count()){
            cursorPosX = cursorPosXNewValue

            cursorX = textChars.subList(0,cursorPosX).fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
        }
    }

    fun setCursorStart() {
        cursorPosX = 0
        cursorX = 0f
    }

    fun setCursorEnd() {
        cursorPosX = textChars.count()
        cursorX = textChars.fold(0f){acc, char -> acc + char.xAdvance * fontSize / 3 }
    }

    fun removeChar(){
        if(cursorPosX > 0)
            textChars.removeAt(--cursorPosX)
    }

    fun removeForwardChar(){
        if(cursorPosX < textChars.count())
            textChars.removeAt(cursorPosX)
    }

    fun insertChar(char: Char){
        textChars.add(cursorPosX++,convertChar(char))
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)

        shaderProgram.setUniform("color", color)
        shaderProgram.setUniform("transformationMatrix" , getLocalModelMatrix(),false)
    }

    override fun render(shaderProgram: ShaderProgram) {
        mesh?.render(shaderProgram)
    }

    final override fun refresh() {
        (widthConstraint as TextScaleConstrain).relativeScale = length / getWorldScale().x
        (heightConstraint as TextScaleConstrain).relativeScale = height / getWorldScale().y

        clearTransformation()
        translateLocal(translateXConstraint.getTranslate(this), translateYConstraint.getTranslate(this))
        children.forEach { it.refresh() }

        val mat = getLocalModelMatrix()

        //DisplayAspectRatio
//        val aspectRatioMultiply = (16f/9f) / (WindowStats.windowWidth.toFloat() / WindowStats.windowHeight)
//        mat.set(0, 0, mat.get(0,0) * aspectRatioMultiply)

        val translateColumn = Vector4f()
        mat.getColumn(3, translateColumn)

        val globalScale = getWorldScale()

        translateColumn.x *= globalScale.x
        translateColumn.y *= globalScale.y

        mat.setColumn(3, translateColumn)

        val globalTranslate = getParentWorldPosition()
        mat.translate(globalTranslate)


        mat.translate(Vector3f(-length, height,0f))

        modelMatrix = mat

    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
        mesh?.cleanup()
    }

}