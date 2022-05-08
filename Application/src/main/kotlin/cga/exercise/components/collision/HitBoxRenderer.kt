package cga.exercise.components.collision

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL33.glVertexAttribDivisor
import java.lang.Float.floatToIntBits
import kotlin.random.Random


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
    private var vboMat = 0

    init {
        // generate IDs
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0,3, GL_FLOAT,false,0,0L)

        val vertexDataMat = mutableListOf<Float>()

        repeat(10000){
            var mat = Matrix4f()

            mat = mat.translate(Vector3f(Random.nextInt(0,1000).toFloat(), Random.nextInt(0,1000).toFloat(), Random.nextInt(0,1000).toFloat()))

            vertexDataMat.add(mat.m00());vertexDataMat.add(mat.m01());vertexDataMat.add(mat.m02());vertexDataMat.add(mat.m03())
            vertexDataMat.add(mat.m10());vertexDataMat.add(mat.m11());vertexDataMat.add(mat.m12());vertexDataMat.add(mat.m13())
            vertexDataMat.add(mat.m20());vertexDataMat.add(mat.m21());vertexDataMat.add(mat.m22());vertexDataMat.add(mat.m23())
            vertexDataMat.add(mat.m30());vertexDataMat.add(mat.m31());vertexDataMat.add(mat.m32());vertexDataMat.add(mat.m33())
            vertexDataMat.add(Random.nextInt(0,2).toFloat())
        }

        vboMat = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboMat)
        glBufferData(GL_ARRAY_BUFFER, vertexDataMat.toFloatArray(), GL_STATIC_DRAW)

        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 68, 0)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 68, 16)
        glEnableVertexAttribArray(3)
        glVertexAttribPointer(3, 4, GL_FLOAT, false, 68, (2 * 16))
        glEnableVertexAttribArray(4)
        glVertexAttribPointer(4, 4, GL_FLOAT, false, 68, (3 * 16))

        glEnableVertexAttribArray(5)
        glVertexAttribPointer(5, 1, GL_FLOAT, false, 68, (4 * 16))

        glVertexAttribDivisor(1, 1)
        glVertexAttribDivisor(2, 1)
        glVertexAttribDivisor(3, 1)
        glVertexAttribDivisor(4, 1)
        glVertexAttribDivisor(5, 1)

        //Unbind
        glBindVertexArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    fun add(hitBox: HitBox) {
        hitboxes.add(hitBox)
    }

    fun updateModelMatrix(){

        val vertexDataMat = mutableListOf<Float>()

        repeat(10000){
            var mat = Matrix4f()

            mat = mat.translate(Vector3f(Random.nextInt(0,1000).toFloat(), Random.nextInt(0,1000).toFloat(), Random.nextInt(0,1000).toFloat()))

            vertexDataMat.add(mat.m00());vertexDataMat.add(mat.m01());vertexDataMat.add(mat.m02());vertexDataMat.add(mat.m03())
            vertexDataMat.add(mat.m10());vertexDataMat.add(mat.m11());vertexDataMat.add(mat.m12());vertexDataMat.add(mat.m13())
            vertexDataMat.add(mat.m20());vertexDataMat.add(mat.m21());vertexDataMat.add(mat.m22());vertexDataMat.add(mat.m23())
            vertexDataMat.add(mat.m30());vertexDataMat.add(mat.m31());vertexDataMat.add(mat.m32());vertexDataMat.add(mat.m33())
            vertexDataMat.add(Random.nextInt(0,2).toFloat())
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboMat)
        glBufferData(GL_ARRAY_BUFFER, vertexDataMat.toFloatArray(), GL_STREAM_DRAW)

    }

    fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glEnable(GL_PROGRAM_POINT_SIZE);
        glPointSize(10f)

        glBindVertexArray(vao)

        glDrawArraysInstanced(GL_LINE_STRIP,0, 16, 10000)
        glBindVertexArray(0)

//        it.bind(shaderProgram)
//        glDrawArraysInstanced(GL_LINE_STRIP,0, 16,1)
//        it.afterRender(shaderProgram)

        glDisable(GL_PROGRAM_POINT_SIZE);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    fun cleanup(){
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }

}