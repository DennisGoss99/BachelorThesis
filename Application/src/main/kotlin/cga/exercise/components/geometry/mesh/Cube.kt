package cga.exercise.components.geometry.mesh

import cga.exercise.components.collision.EndPoint
import cga.exercise.components.collision.ICollisionBox
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11

class Cube(override val id : Int) : ICollisionBox, Transformable(){
    override val minEndPoints = arrayOf(EndPoint(this,-1f,true),EndPoint(this,-1f,true),EndPoint(this,-1f,true))
    override val maxEndPoints = arrayOf(EndPoint(this,-1f,false),EndPoint(this,-1f,false),EndPoint(this,-1f,false))


    override var collided = false
    override var collidedWith = mutableListOf<ICollisionBox>()

    private val vertexData = floatArrayOf(
        -1f,1f,1f,
        -1f,-1f,1f,
        1f,-1f,1f,
        1f,1f,1f,
        -1f,1f,-1f,
        -1f,-1f,-1f,
        1f,-1f,-1f,
        1f,1f,-1f
    )

    private val index = intArrayOf(
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

    private val vao = arrayOf(VertexAttribute(3, GL11.GL_FLOAT,0,0))

    val mesh = Mesh(vertexData, index, vao, null)

    init {

    }

    override fun getCoordinates(): Array<Float> {
        val mat = getWorldModelMatrix()
        val max = Vector4f(1f,1f,1f, 1f).mul(mat)
        val min = Vector4f(1f,1f,1f, -1f).mul(mat).mul(-1f)

        return arrayOf(min.x, max.x, min.y, max.y, min.z, max.z)
    }

    fun updateCube(){
        val mat = getWorldModelMatrix()
        val max = Vector4f(1f,1f,1f, 1f).mul(mat)
        val min = Vector4f(1f,1f,1f, -1f).mul(mat).mul(-1f)

        minEndPoints[0].value = min.x
        maxEndPoints[0].value = max.x
        minEndPoints[1].value = min.y
        maxEndPoints[1].value = max.y
        minEndPoints[2].value = min.z
        maxEndPoints[2].value = max.z
    }

    fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        shaderProgram.setUniform("model_matrix" , getWorldModelMatrix(),false)
        shaderProgram.setUniform("collision", if(collided) 1 else 0)
    }

    fun render(shaderProgram: ShaderProgram){
        mesh.render(shaderProgram)
    }


}