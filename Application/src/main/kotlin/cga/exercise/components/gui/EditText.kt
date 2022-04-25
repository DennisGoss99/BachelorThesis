package cga.exercise.components.gui


import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.gui.TextComponents.TextCursor
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import java.util.*

class EditText (var text : String,
                var fontSize : Float,
                val font : FontType,
                val maxLineLength : Float,
                val centeredX : Boolean,
                val centeredY : Boolean,
                translate : Vector2f = Vector2f(0f,0f),
                override var color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : GuiElement(listOf()) {

    private var mesh: Mesh

    var cursorX = 0f
    var cursorY = 0f

    var cursorPosX = 0
//        private set

    val textChars = mutableListOf<cga.exercise.components.text.Char>()

    private var vertexData = mutableListOf<Float>()
    private var iboData = mutableListOf<Int>()
    private var iboCursor = 0

    var length = 0f
    private var height = 0f

    private val vao = arrayOf(
        VertexAttribute(2, GL11.GL_FLOAT, 16, 0),
        VertexAttribute(2, GL11.GL_FLOAT, 16, 8)
    )

    init {
        children = listOf(
            TextCursor(Vector2f(0.0005f * fontSize,0.009f * fontSize), color = Color(220,220,220)))

        text.forEach { c ->
            setLetter(convertChar(c), fontSize / 3)
        }
        cursorPosX = text.length

        mesh = Mesh(vertexData.toFloatArray(), iboData.toIntArray(), vao, font.fontImageMaterial)


        //translate.y -= 0.002f * fontSize

        translateLocal(translate)
    }

    private fun convertChar(character: Char) : cga.exercise.components.text.Char{
        return font.chars[character.code] ?: throw Exception("Character couldn't be found in $font: [$character]")
    }

    private fun setLetter(fontTypeChar: cga.exercise.components.text.Char, fontSize: Float) {

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

        textChars.add(fontTypeChar)

        cursorX += fontTypeChar.xAdvance * fontSize
        length += fontTypeChar.xAdvance * fontSize
    }

    private fun addVertices(x: Float,y: Float,maxX: Float,maxY: Float,texx: Float, texy: Float, texmaxX: Float, texmaxY: Float) {
        Collections.addAll(
            vertexData,
            x, y, texx, texy,
            x, maxY, texx, texmaxY,
            maxX, maxY, texmaxX, texmaxY,
            maxX, y, texmaxX, texy
        )

        Collections.addAll(
            iboData,
            iboCursor, iboCursor + 1, iboCursor + 2,
            iboCursor + 2, iboCursor + 3, iboCursor
        )

        iboCursor += 4
    }

    fun textHasChanged(){
        mesh.cleanupMesh()

        vertexData.clear()
        iboData.clear()
        iboCursor = 0
        length = 0f
        height = 0f

        val cpyTextChars = textChars.toList()

        textChars.clear()
        cursorX = 0f

        cpyTextChars.forEach {
            setLetter(it, fontSize/3)
        }

        moveCursor(0)

        mesh = Mesh(vertexData.toFloatArray(), iboData.toIntArray(), vao, font.fontImageMaterial)
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

        val mat = getLocalModelMatrix()

        val translateColumn = Vector4f()
        mat.getColumn(3, translateColumn)

        val globalScale = getWorldScale()

        translateColumn.x *= globalScale.x
        translateColumn.y *= globalScale.y

        mat.setColumn(3, translateColumn)

        val globalTranslate = getParentWorldPosition()
        mat.translate(globalTranslate)

        if (centeredX || centeredY)
            mat.translate(Vector3f(if(centeredX) -length else 0f, if(centeredY) 0.01f * fontSize else 0f,0f))

        shaderProgram.setUniform("color", color)
        shaderProgram.setUniform("transformationMatrix" , mat,false)
    }

    override fun render(shaderProgram: ShaderProgram) {
        mesh.render(shaderProgram)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
        mesh.cleanup()
    }

}