package cga.exercise.components.properties.collision

import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import java.util.concurrent.atomic.AtomicBoolean

open class HitBox(override val id : Int, pos : Vector3f = Vector3f(0f), scale : Vector3f = Vector3f(1f)) : IHitBox, Transformable(){
    override val minEndPoints = arrayOf(EndPoint(this,-1f,true), EndPoint(this,-1f,true), EndPoint(this,-1f,true))
    override val maxEndPoints = arrayOf(EndPoint(this,-1f,false), EndPoint(this,-1f,false), EndPoint(this,-1f,false))

    override var collisionChecked = AtomicBoolean(false)

    override var collided = AtomicBoolean(false)
    override var collidedWith  = mutableListOf<IHitBox>()

    init {
        translateLocal(pos)
        scaleLocal(scale)
        updateEndPoints()
    }

    @Synchronized
    override fun addCollidedWith(hitBox: IHitBox) {
        collidedWith.add(hitBox)
    }

    @Synchronized
    override fun removeCollidedWith(hitBox: IHitBox) {
        collidedWith.remove(hitBox)
    }

    override fun updateEndPoints(){
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
}