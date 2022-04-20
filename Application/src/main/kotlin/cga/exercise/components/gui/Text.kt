package cga.exercise.components.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.gui.oldGuiElement
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.transformable.Transformable2D
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.util.*

class Text (var text : String,
            var fontSize : Float,
            val font : FontType,
            val maxLineLength : Float,
            val centered : Boolean,
            translate : Vector2f = Vector2f(0f,0f),
            override var color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : GuiElement() {

    private var mesh: Mesh
    private var cursorX = 0f
    private var cursorY = 0f

    private var vertexData = mutableListOf<Float>()
    private var iboData = mutableListOf<Int>()
    private var iboCursor = 0

    private var length = 0f
    private var height = 0f



    private val vao = arrayOf(
        VertexAttribute(2, GL11.GL_FLOAT, 16, 0),
        VertexAttribute(2, GL11.GL_FLOAT, 16, 8)
    )

    init {
        text.forEach { c ->
            setLetter(c, fontSize / 3)
        }



        mesh = Mesh(vertexData.toFloatArray(), iboData.toIntArray(), vao, font.fontImageMaterial)


        //unify all translate coordinates
        translate.x *= 2
        translate.y *= -2

        translate.y += 0.002f * fontSize

        if (centered) {
            translate.x -= length
        }

        translateLocal(translate)
    }

    private fun setLetter(character: Char, fontSize: Float) {
        val fontTypeChar =
            font.chars[character.toInt()] ?: throw Exception("Character couldn't be found in $font: [$character]")

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

    fun textHasChanged() {
        mesh.cleanupMesh()

        vertexData.clear()
        iboData.clear()
        iboCursor = 0
        cursorX = 0f
        cursorY = 0f
        length = 0f
        height = 0f

        text.forEach { c ->
            setLetter(c, fontSize / 3)
        }

        mesh = Mesh(vertexData.toFloatArray(), iboData.toIntArray(), vao, font.fontImageMaterial)
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
        shaderProgram.setUniform("color", color)
    }

    override fun render(shaderProgram: ShaderProgram) {
//        GL30.glEnable(GL11.GL_BLEND)
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
//        GL11.glDisable(GL11.GL_DEPTH_TEST)

        mesh.render(shaderProgram)

//        GL11.glEnable(GL11.GL_DEPTH_TEST)
//        GL30.glDisable(GL11.GL_BLEND)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
        mesh.cleanup()
    }
}