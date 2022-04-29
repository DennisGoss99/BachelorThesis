package cga.exercise.components.geometry.mesh

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30

class SimpleMesh(vertexdata: FloatArray, attributes: Array<VertexAttribute>, var material: IMaterial?) {

    //private data
    private var vao = 0
    private var vbo = 0
    private var vertexcount = 0

    init {
        vertexcount = vertexdata.size

        // generate IDs
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        vbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexdata, GL30.GL_STATIC_DRAW)

        for(i in attributes.indices){
            glEnableVertexAttribArray(i);
            glVertexAttribPointer(i, attributes[i].size, attributes[i].type, false, attributes[i].stride, attributes[i].offset.toLong())
            vertexcount /= attributes[i].size
        }

        //Unbind
        GL30.glBindVertexArray(0);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    /**
     * renders the mesh
     */
    private fun render() {
        // activate VAO
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(0)

        // render call
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0, vertexcount)

        GL30.glBindVertexArray(0)
        GL20.glDisableVertexAttribArray(0)
    }

    fun render(shaderProgram: ShaderProgram) {
        material?.bind(shaderProgram)
        render()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanupMesh(){
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }

    fun cleanup() {
        cleanupMesh()
        material?.cleanup()
    }


}