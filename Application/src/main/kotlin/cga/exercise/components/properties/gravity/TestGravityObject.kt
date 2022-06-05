package cga.exercise.components.properties.gravity

import org.joml.Vector3f

class TestGravityObject(override var mass: Float, override var gravityProperty: GravityProperties, var pos : Vector3f, override var velocity : Vector3f = Vector3f(0f, 0f, 0f)) : IGravity {

    override val id: Int = 0

    override var acceleration: Vector3f = Vector3f(0f, 0f, 0f)

    override fun applyObjectForce() {
        velocity.add(acceleration)
        acceleration = Vector3f(0f, 0f, 0f)
        pos.add(velocity)
    }

    override fun getPosition(): Vector3f = Vector3f(pos)


}