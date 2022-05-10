package cga.exercise.components.gravity

import cga.exercise.components.collision.HitBox
import org.joml.Vector3f

class Test(var hitBox: HitBox, override var mass: Float, override var velocity : Vector3f = Vector3f(0f, 0f, 0f)) : IGravity{

    override var deltaVelocity : Vector3f = Vector3f(0f, 0f, 0f)

    override fun applyObjectForce() {
        velocity.add(deltaVelocity)
        deltaVelocity = Vector3f(0f)
    }

    override fun getPosition(): Vector3f = hitBox.getPosition()
    override fun move() {
        hitBox.translateLocal(velocity)
        hitBox.updateEndPoints()
    }


}