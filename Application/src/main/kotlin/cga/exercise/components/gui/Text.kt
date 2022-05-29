package cga.exercise.components.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.gui.TextComponents.TextMode
import cga.exercise.components.gui.constraints.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import cga.exercise.game.StaticResources
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import java.util.*

open class Text(text : String,
                fontSize : Float,
                val font : FontType,
                val maxLineLength : Float,
                var textMode : TextMode,
                var multiline : Boolean = false,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                override var color : Color = StaticResources.fontColor) : GuiElement(TextScaleConstrain(),TextScaleConstrain(), translateXConstraint, translateYConstraint, children = listOf()) {

    var fontSize : Float = 0.0f
        set(value) {
            field = value / 3f
        }

    var text
        get() = textChars.fold(""){acc, chars -> acc + chars.fold(""){acc2, char -> acc2 + char.id.toChar() } }
        set(value) {
            textChars.clear()
            textChars.add(mutableListOf())
            value.forEach { c ->
                addTextChat(convertChar(c))
            }
            textHasChanged()
            refresh()
        }

    var cursorX = 0f
        protected set
    var cursorY = 0f
        protected set

    var cursorPosX = 0
        protected set
    var cursorPosY = 0
        protected set

    protected val textChars = mutableListOf<MutableList<cga.exercise.components.text.Char>>()

    private var mesh : SimpleMesh? = null

    private var vertexData = mutableListOf<Float>()

    private var length = 0f

    private var heightText = 0f;

    protected var lineHeight = 0f

    private var lineCount = 0

    private val vao = arrayOf(
        VertexAttribute(2, GL11.GL_FLOAT, 16, 0),
        VertexAttribute(2, GL11.GL_FLOAT, 16, 8)
    )

    init {
        this.fontSize = fontSize

        textChars.add(mutableListOf())

        text.forEach { c ->
            addTextChat(convertChar(c))
        }

        cursorPosY = textChars.size - 1
        cursorPosX = textChars[cursorPosY].count()

        this.textHasChanged()
    }

    private fun addTextChat(char: cga.exercise.components.text.Char){
        if(char.id == 10) {
            if(!multiline)
                return

            textChars.add(mutableListOf())
            cursorPosY++
        }
        else
            textChars[cursorPosY].add(char)
    }

    private fun convertChar(character: Char) : cga.exercise.components.text.Char{
        return font.chars[character.code] ?: throw Exception("Character couldn't be found in $font: [$character]")
    }

    protected fun getMaxLength() : Float {
        return textChars.fold(0f){
                acc, chars ->
            val value = chars.fold(0f){acc2, char -> acc2 + char.xAdvance * fontSize }
            if(acc < value)
                value
            else
                acc
        }
    }

    private fun setLetter(fontTypeChar: cga.exercise.components.text.Char) {

        val x = cursorX + fontTypeChar.xOffset * fontSize
        val y = cursorY + fontTypeChar.yOffset * fontSize
        val maxX = x + fontTypeChar.sizeX * fontSize
        val maxY = y + fontTypeChar.sizeY * fontSize
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

        cursorX += fontTypeChar.xAdvance * fontSize
        textChars[lineCount].add(fontTypeChar)
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

    open fun textHasChanged(){
        mesh?.cleanupMesh()

        vertexData.clear()

        lineHeight = 0.03f * fontSize
        heightText = 0f

        //get max line length
        length = getMaxLength()
        var lineLength: Float

        cursorY = when(translateYConstraint){
            is Center -> 0f
            else -> fontSize * -0.005f
        }

        lineCount = 0

        val cpyTextChars = textChars.toList()

        textChars.clear()

        cpyTextChars.forEachIndexed { index, it ->
            textChars.add(mutableListOf())

            lineLength = it.fold(0f){acc, char -> acc + char.xAdvance * fontSize }
            cursorX = when(textMode){
                TextMode.Center -> (length - lineLength) / 2f
                TextMode.Left -> 0f
                TextMode.Right -> length - lineLength
            }

            it.forEach { c ->
                setLetter(c)
            }

            cursorX = 0f
            cursorY += lineHeight
            heightText += lineHeight
            lineCount++
        }

        mesh = SimpleMesh(vertexData.toFloatArray(), vao, font.fontImageMaterial)
    }

    fun removeChar(){
        if(!(cursorPosY == 0 && cursorPosX == 0)){
            if(cursorPosX == 0 ){
                cursorPosX = textChars[cursorPosY-1].count()
                textChars[cursorPosY - 1].addAll(textChars[cursorPosY])
                textChars.removeAt(cursorPosY)
                cursorPosY--
            }else
                textChars[cursorPosY].removeAt(--cursorPosX)
        }
    }

    fun removeForwardChar(){

        if(!(cursorPosY == textChars.size - 1 && textChars[cursorPosY].size == cursorPosX)){
            if(textChars[cursorPosY].size ==cursorPosX){
                textChars[cursorPosY].addAll(textChars[cursorPosY + 1])
                textChars.removeAt(cursorPosY + 1)
            }else
                textChars[cursorPosY].removeAt(cursorPosX)
        }
    }

    fun insertChar(char: Char){
        val c = convertChar(char)

        if(c.id == 10) {
            if(!multiline)
                return

            val currentList = textChars[cursorPosY]
            val sublist = currentList.subList(cursorPosX, currentList.size).toMutableList()
            repeat(currentList.size - cursorPosX){ currentList.removeLast() }
            textChars.add(++cursorPosY, sublist)
            cursorPosX = 0
        }else
            textChars[cursorPosY].add(cursorPosX++,c)
    }

    override fun bind(shaderProgram: ShaderProgram) {
        mesh?.bind(shaderProgram)
        shaderProgram.setUniform("elementColor", color)
        shaderProgram.setUniform("transformationMatrix" , getLocalModelMatrix(),false)
    }

    override fun render(shaderProgram: ShaderProgram) {
        mesh?.render(shaderProgram)
    }

    final override fun refresh() {
        (widthConstraint as TextScaleConstrain).relativeScale = length / getWorldScale().x
        (heightConstraint as TextScaleConstrain).relativeScale = heightText / getWorldScale().y

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


        mat.translate(Vector3f(-length, heightText,0f))

        modelMatrix = mat

    }

    override fun updateVariables() {
        textHasChanged()
        super.updateVariables()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
        mesh?.cleanup()
    }

    override fun getHeight(): Float {
        return fontSize * 0.01f + (lineHeight * (lineCount -1))
    }

}