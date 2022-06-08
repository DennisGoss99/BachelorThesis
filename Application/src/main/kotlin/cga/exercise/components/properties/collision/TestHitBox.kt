package cga.exercise.components.properties.collision

import org.joml.Vector3f
import java.util.concurrent.atomic.AtomicBoolean

class TestHitBox : IHitBox {


    override val id: Int
    override val minEndPoints: Array<EndPoint>
    override val maxEndPoints: Array<EndPoint>

    override var collided: AtomicBoolean
    override var collisionChecked: AtomicBoolean

    override var collidedWith: MutableList<IHitBox>


    constructor(id: Int, x1: Float, x2: Float, y1: Float, y2: Float, z1: Float, z2: Float) {
        this.id = id
        this.minEndPoints = arrayOf(EndPoint(this, x1, true), EndPoint(this, y1, true), EndPoint(this, z1, true))
        this.maxEndPoints = arrayOf(EndPoint(this, x2, false), EndPoint(this, y2, false), EndPoint(this, z2, false))
        this.collided = AtomicBoolean(false)
        this.collisionChecked = AtomicBoolean(false)
        this.collidedWith = mutableListOf<IHitBox>()
    }

    constructor(id: Int, pos : Vector3f, scale : Float = 1f) {
        this.id = id
        this.minEndPoints = arrayOf(EndPoint(this, pos.x - scale, true), EndPoint(this, pos.y - scale, true), EndPoint(this, pos.z - scale, true))
        this.maxEndPoints = arrayOf(EndPoint(this, pos.x + scale, false), EndPoint(this, pos.y + scale, false), EndPoint(this, pos.z + scale, false))

        this.collided = AtomicBoolean(false)
        this.collisionChecked = AtomicBoolean(false)
        this.collidedWith = mutableListOf<IHitBox>()
    }

    @Synchronized
    override fun addCollidedWith(hitBox : IHitBox){
        collidedWith.add(hitBox)
    }

    @Synchronized
    override fun removeCollidedWith(hitBox: IHitBox) {
        collidedWith.remove(hitBox)
    }


    override fun updateEndPoints() {
        //
    }

    override fun translateLocal(vec: Vector3f) {
        //
    }

}