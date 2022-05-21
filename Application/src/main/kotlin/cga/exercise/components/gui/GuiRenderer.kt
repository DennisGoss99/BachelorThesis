package cga.exercise.components.gui

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30

class GuiRenderer(private val shaderProgram: ShaderProgram) {

    private var vao = 0
    private var vbo = 0

    val VBO = floatArrayOf(
        -1f, 1f,
        -1f,-1f,
        1f, 1f,
        1f, -1f)

    val VAO = VertexAttribute(2, GL11.GL_FLOAT,0,0)

    init {

        // generate IDs
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        vbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, VBO, GL30.GL_STATIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, VAO.size, VAO.type, false, VAO.stride, VAO.offset.toLong())

        //Unbind
        GL30.glBindVertexArray(0);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);


    }

    private var lastElement = -1
    private fun doRender(guiElement: GuiElement, dt: Float, t: Float) {
        if(guiElement.isVisible){


            when(guiElement){
                is Text -> {
                    if(lastElement != 1) {
                        shaderProgram.setUniform("elementType", 1)
                        lastElement = 1
                    }

                    guiElement.bind(shaderProgram)
                    guiElement.render(shaderProgram)
                }
                is LayoutBox -> {
                }
                else -> {
                    if(lastElement != 0) {
                        GL30.glBindVertexArray(vao)
                        GL20.glEnableVertexAttribArray(0)
                        shaderProgram.setUniform("elementType", 0)
                        lastElement = 0
                    }

                    guiElement.bind(shaderProgram)
                    guiElement.render(shaderProgram)
                    GL11.glDrawArrays(GL_TRIANGLE_STRIP ,0, 4)
                }
            }

            guiElement.afterRender(shaderProgram)

            guiElement.children.forEach { doRender(it,dt,t)}
            guiElement.afterChildrenRender(shaderProgram)

        }
    }

    fun beforeGUIRender(){
        lastElement = -1
        GL30.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_DEPTH_TEST)

        shaderProgram.use()
    }

    fun render(guiElement: GuiElement, dt: Float, t: Float) {
        doRender(guiElement, dt , t)
    }

    fun afterGUIRender(){
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL30.glDisable(GL11.GL_BLEND)

        // call the rendering method every frame
        GL20.glDisableVertexAttribArray(0)
        GL30.glBindVertexArray(0)
    }


    fun cleanup() {
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }

}