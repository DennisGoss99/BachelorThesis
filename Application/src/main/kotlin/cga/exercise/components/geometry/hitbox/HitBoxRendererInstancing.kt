package cga.exercise.components.geometry.hitbox

import cga.exercise.components.properties.collision.HitBox
import cga.exercise.components.shader.ShaderProgram
import cga.framework.printTimeMillis
import cga.framework.printlnTimeMillis
import kotlinx.coroutines.*
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL33.glVertexAttribDivisor


class HitBoxRendererInstancing(hitBoxes : MutableList<HitBox> = mutableListOf()) : IHitBoxRenderer {

    override var hitboxes : MutableList<HitBox> = hitBoxes
        set(value) {
            vertexDataMat = FloatArray(count * 17)
            field = value
        }

    private var vertexDataMat = FloatArray(count * 17)

    private val vertexData = floatArrayOf(

// Line Model
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

// Cube Model
//        -1f,-1f,1f,
//        -1f,-1f,-1f,
//        1f,-1f,1f,
//        -1f,-1f,-1f,
//        1f,-1f,-1f,
//        1f,-1f,1f,
//
//        -1f,-1f,1f,
//        1f,-1f,1f,
//        -1f,1f,1f,
//        -1f,1f,1f,
//        1f,-1f,1f,
//        1f,1f,1f,
//
//        -1f,1f,1f,
//        1f,1f,1f,
//        -1f,1f,-1f,
//        -1f,1f,-1f,
//        1f,1f,1f,
//        1f,1f,-1f,
//
//        -1f,1f,-1f,
//        1f,1f,-1f,
//        -1f,-1f,-1f,
//        -1f,-1f,-1f,
//        1f,1f,-1f,
//        1f,-1f,-1f,
//
//        -1f,-1f,1f,
//        -1f,1f,-1f,
//        -1f,-1f,-1f,
//        -1f,-1f,1f,
//        -1f,1f,1f,
//        -1f,1f,-1f,
//
//        1f,-1f,1f,
//        1f,-1f,-1f,
//        1f,1f,-1f,
//        1f,-1f,1f,
//        1f,1f,-1f,
//        1f,1f,1f,
//0        -1f,1f,1f,
//1        -1f,-1f,1f,
//2        1f,-1f,1f,
//3        1f,1f,1f,
//4        -1f,1f,-1f,
//5        -1f,-1f,-1f,
//6        1f,-1f,-1f,
//7        1f,1f,-1f,
    )

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

        vertexDataMat = FloatArray(count * 17)

        for(i in 0 until count * 17 step 17){

            val hitBox = hitboxes[i/17]
            var mat = hitBox.getWorldModelMatrix()

            vertexDataMat[i     ] = mat[0,0]; vertexDataMat[i + 1 ] = mat[0,1]; vertexDataMat[i + 2 ] = mat[0,2]; vertexDataMat[i + 3 ] = mat[0,3]
            vertexDataMat[i + 4 ] = mat[1,0]; vertexDataMat[i + 5 ] = mat[1,1]; vertexDataMat[i + 6 ] = mat[1,2]; vertexDataMat[i + 7 ] = mat[1,3]
            vertexDataMat[i + 8 ] = mat[2,0]; vertexDataMat[i + 9 ] = mat[2,1]; vertexDataMat[i + 10] = mat[2,2]; vertexDataMat[i + 11] = mat[2,3]
            vertexDataMat[i + 12] = mat[3,0]; vertexDataMat[i + 13] = mat[3,1]; vertexDataMat[i + 14] = mat[3,2]; vertexDataMat[i + 15] = mat[3,3]
            vertexDataMat[i + 16] = if(hitBox.collided.get()) 1f else 0f
        }

        vboMat = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboMat)
        glBufferData(GL_ARRAY_BUFFER, vertexDataMat, GL_STATIC_DRAW)

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

    override fun add(hitBox: HitBox) {
        if(!hitboxes.contains(hitBox))
            hitboxes.add(hitBox)
        vertexDataMat = FloatArray(count * 17)
    }

    override fun removeHitBoxID(id : Int){
        hitboxes.removeAll{it.id == id}
    }

    override fun clear(){
        hitboxes.clear()
        vertexDataMat = FloatArray(0)
    }

    override fun updateModelMatrix(){

        glBindBuffer(GL_ARRAY_BUFFER, vboMat)


//        for(i in 0 until count * 17 step 17){
//
//            val hitBox = hitboxes[i/17]
//            val mat = hitBox.getWorldModelMatrix()
//
//            vertexDataMat[i     ] = mat[0,0]; vertexDataMat[i + 1 ] = mat[0,1]; vertexDataMat[i + 2 ] = mat[0,2]; vertexDataMat[i + 3 ] = mat[0,3]
//            vertexDataMat[i + 4 ] = mat[1,0]; vertexDataMat[i + 5 ] = mat[1,1]; vertexDataMat[i + 6 ] = mat[1,2]; vertexDataMat[i + 7 ] = mat[1,3]
//            vertexDataMat[i + 8 ] = mat[2,0]; vertexDataMat[i + 9 ] = mat[2,1]; vertexDataMat[i + 10] = mat[2,2]; vertexDataMat[i + 11] = mat[2,3]
//            vertexDataMat[i + 12] = mat[3,0]; vertexDataMat[i + 13] = mat[3,1]; vertexDataMat[i + 14] = mat[3,2]; vertexDataMat[i + 15] = mat[3,3]
//            vertexDataMat[i + 16] = if(hitBox.collided.get()) 1f else 0f
//        }

        for(i in 0 until count){
            val hitBox = hitboxes[i]
            hitBox.getWorldModelMatrix().get(vertexDataMat, i * 17)
            vertexDataMat[i * 17 + 16] = if(hitBox.collided.get()) 1f else 0f
        }

        glBufferData(GL_ARRAY_BUFFER, vertexDataMat, GL_DYNAMIC_DRAW)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun updateModelMatrixParallel(jobCount : Int){

        glBindBuffer(GL_ARRAY_BUFFER, vboMat)

        val chunkSize = count / jobCount
        val remains = count - (chunkSize * jobCount)

        val jobs = mutableListOf<Job>()

        for(jobIndex in 0 until jobCount){
            jobs.add(GlobalScope.launch {
                for(i in jobIndex * chunkSize * 17 until (((jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains) * 17) step 17){
                    val hitBox = hitboxes[i/17]
                    var mat = hitBox.getWorldModelMatrix()

                    vertexDataMat[i     ] = mat[0,0]; vertexDataMat[i + 1 ] = mat[0,1]; vertexDataMat[i + 2 ] = mat[0,2]; vertexDataMat[i + 3 ] = mat[0,3]
                    vertexDataMat[i + 4 ] = mat[1,0]; vertexDataMat[i + 5 ] = mat[1,1]; vertexDataMat[i + 6 ] = mat[1,2]; vertexDataMat[i + 7 ] = mat[1,3]
                    vertexDataMat[i + 8 ] = mat[2,0]; vertexDataMat[i + 9 ] = mat[2,1]; vertexDataMat[i + 10] = mat[2,2]; vertexDataMat[i + 11] = mat[2,3]
                    vertexDataMat[i + 12] = mat[3,0]; vertexDataMat[i + 13] = mat[3,1]; vertexDataMat[i + 14] = mat[3,2]; vertexDataMat[i + 15] = mat[3,3]
                    vertexDataMat[i + 16] = if(hitBox.collided.get()) 1f else 0f
                }
            })
        }

        jobs.joinAll()

        glBufferData(GL_ARRAY_BUFFER, vertexDataMat, GL_STATIC_DRAW)
    }

    override fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
        glEnable(GL_PROGRAM_POINT_SIZE)
        glPointSize(10f)

        glBindVertexArray(vao)

        glDrawArraysInstanced(GL_LINE_STRIP,0, 16, count)
//        glDrawArraysInstanced(GL_TRIANGLES,0, 36, count)
        glBindVertexArray(0)
//
        glDisable(GL_PROGRAM_POINT_SIZE)
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
    }

    override fun cleanup(){
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vboMat != 0) glDeleteBuffers(vboMat)
        if (vao != 0) glDeleteVertexArrays(vao)
    }

}