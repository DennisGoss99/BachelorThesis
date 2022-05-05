package cga.exercise.components.collision

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.*


class HitBoxRenderer(var hitboxes : MutableList<HitBox> = mutableListOf()) {




    private val vertexData = floatArrayOf(
        -1f,1f,1f,
        1f,1f,1f,
        1f,-1f,1f,
        -1f,-1f,1f,
        -1f,-1f,-1f,
        1f,-1f,-1f,
        1f,-1f,1f,
        1f,-1f,-1f,
        1f,1f,-1f,
        1f,1f,1f,
        1f,1f,-1f,
        -1f,1f,-1f,
        -1f,-1f,-1f,
        -1f,-1f,1f,
        -1f,1f,1f,
        -1f,1f,-1f,

//0        -1f,1f,1f,
//1        -1f,-1f,1f,
//2        1f,-1f,1f,
//3        1f,1f,1f,
//4        -1f,1f,-1f,
//5        -1f,-1f,-1f,
//6        1f,-1f,-1f,
//7        1f,1f,-1f,
    )

    //Cube IBO
/*    private val indexData = intArrayOf(
        5,6,1,
        1,6,2,

        2,0,1,
        3,0,2,

        2,6,7,
        2,7,3,

        3,7,0,
        0,7,4,

        0,4,1,
        1,4,5,

        5,4,6,
        6,4,7,
    )
*/

    //Line IBO
/*    private val indexData = intArrayOf(
//        1,2,
//        2,6,
//        6,5,
//        5,1,
//
//        3,0,
//        0,4,
//        4,7,
//        7,3,
//
//        1,0,
//        2,3,
//        5,4,
//        6,7)
*/

    private var vao = 0
    private var vbo = 0

    init {
        // generate IDs
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL11.GL_FLOAT,false,0,0L)

        //Unbind
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    fun add(hitBox: HitBox) {
        hitboxes.add(hitBox)
    }

    fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_PROGRAM_POINT_SIZE);
        glPointSize(10f)
        glBindVertexArray(vao)

        hitboxes.forEach {
            it.bind(shaderProgram)
            glDrawArrays(GL_LINE_STRIP,0, 16)
            it.afterRender(shaderProgram)
        }

        glBindVertexArray(0)
        glDisable(GL_PROGRAM_POINT_SIZE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

    }

    fun cleanup(){
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }

}