package cga.exercise.components.geometry.hitbox

import cga.exercise.components.properties.collision.HitBox
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.shader.ShaderProgram
import kotlinx.coroutines.*
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL32
import org.lwjgl.opengl.GL33

class HitBoxRendererUniqueModel(override var hitboxes : MutableList<HitBox> = mutableListOf()) : IHitBoxRenderer {



    private val vertexData = floatArrayOf(
        -1f,1f,1f,
        -1f,-1f,1f,
        1f,-1f,1f,
        1f,1f,1f,
        -1f,1f,-1f,
        -1f,-1f,-1f,
        1f,-1f,-1f,
        1f,1f,-1f,

//0        -1f,1f,1f,
//1        -1f,-1f,1f,
//2        1f,-1f,1f,
//3        1f,1f,1f,
//4        -1f,1f,-1f,
//5        -1f,-1f,-1f,
//6        1f,-1f,-1f,
//7        1f,1f,-1f,
    )

    private val indexData = intArrayOf(
        1,5,2,
        5,6,2,

        1,2,0,
        0,2,3,

        0,3,4,
        4,3,7,

        4,7,5,
        5,7,6,

        1,4,5,
        1,0,4,

        2,6,7,
        2,7,3,
    )

    private val meshes : MutableList<Mesh> = MutableList(hitboxes.size){Mesh(vertexData, indexData, arrayOf(VertexAttribute(3,GL32.GL_FLOAT,0,0)),null)}

    override fun add(hitBox: HitBox) {
        hitboxes.add(hitBox)
        meshes.add(Mesh(vertexData, indexData, arrayOf(VertexAttribute(3,GL32.GL_FLOAT,0,0)),null))
    }

    override fun removeHitBoxID(id : Int){
        val index = hitboxes.indexOfFirst { it.id == id }
        hitboxes.removeAt(index)
        meshes[index].cleanup()
        meshes.removeAt(index)
    }

    override fun clear(){
        hitboxes.clear()
        meshes.forEach { it.cleanup() }
        meshes.clear()
    }

    override fun updateModelMatrix(){}

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun updateModelMatrixParallel(jobCount : Int){}

    override fun render(shaderProgram: ShaderProgram){
        shaderProgram.use()


//        GL32.glPolygonMode(GL32.GL_FRONT_AND_BACK, GL32.GL_LINE);
//        GL32.glEnable(GL32.GL_PROGRAM_POINT_SIZE);
//        GL32.glPointSize(10f)

        meshes.forEachIndexed { index, it ->
            shaderProgram.setUniform("collision_in", if(hitboxes[index].collided.get()) 1f else 0f)
            shaderProgram.setUniform("model_matrix", hitboxes[index].getWorldModelMatrix(), false)
            it.render(shaderProgram)
        }

//        GL32.glDrawArraysInstanced(GL32.GL_LINE_STRIP, 0, 16, count)
        GL32.glBindVertexArray(0)

//        GL32.glDisable(GL32.GL_PROGRAM_POINT_SIZE);
//        GL32.glPolygonMode(GL32.GL_FRONT_AND_BACK, GL32.GL_FILL);
    }

    override fun cleanup(){
        meshes.forEach { it.cleanup() }
    }

}