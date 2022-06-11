package cga.exercise.components.properties.applier

import cga.exercise.components.properties.collision.EndPoint
import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.gravity.GravityProperties
import org.joml.Vector3f
import java.util.concurrent.atomic.AtomicBoolean

class TestApplierObject(
    override val id: Int,
    override var mass: Float,
    pos : Vector3f,
    scale : Vector3f,
    override var velocity: Vector3f,
    override var gravityProperty: GravityProperties,
    override var interact: Boolean
) :  IApplier{

    override var checked: AtomicBoolean = AtomicBoolean(false)

    var pos : Vector3f = pos
        set(value) {
            field = value
            updateEndPoints()
        }

    var scale : Vector3f = scale
        set(value) {
            field = value
            updateEndPoints()
        }

    override val minEndPoints: Array<EndPoint> =
        arrayOf(EndPoint(this, pos.x - scale.x, true), EndPoint(this, pos.y - scale.y, true), EndPoint(this, pos.z - scale.z, true))

    override val maxEndPoints: Array<EndPoint> =
        arrayOf(EndPoint(this, pos.x + scale.x, false), EndPoint(this, pos.y + scale.y, false), EndPoint(this, pos.z + scale.z, false))

    override var collided: AtomicBoolean = AtomicBoolean(false)
    override var collisionChecked: AtomicBoolean = AtomicBoolean(false)

    override var collidedWith: MutableList<IHitBox> = mutableListOf<IHitBox>()

    override var acceleration: Vector3f = Vector3f(0f)

    override fun applyObjectForce() {
        velocity.add(acceleration)
        acceleration = Vector3f(0f, 0f, 0f)
        translateLocal(velocity)
    }

    override fun getPosition(): Vector3f = pos


    override fun addCollidedWith(hitBox: IHitBox) {
        collidedWith.add(hitBox)
    }

    override fun removeCollidedWith(hitBox: IHitBox) {
        collidedWith.remove(hitBox)
    }

    override fun updateEndPoints() {
        minEndPoints[0].value = pos.x - scale.x
        maxEndPoints[0].value = pos.x + scale.x

        minEndPoints[1].value = pos.y - scale.y
        maxEndPoints[1].value = pos.y + scale.y

        minEndPoints[2].value = pos.z - scale.z
        maxEndPoints[2].value = pos.z + scale.z
    }

    override fun translateLocal(vec: Vector3f) {
        pos.add(vec)
        updateEndPoints()
    }
}