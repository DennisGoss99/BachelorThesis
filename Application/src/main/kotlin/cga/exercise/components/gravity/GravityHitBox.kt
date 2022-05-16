package cga.exercise.components.gravity

import cga.exercise.components.collision.HitBox
import org.joml.Vector3f

class GravityHitBox(var hitBox: HitBox, override var mass: Float, override var velocity : Vector3f = Vector3f(0f, 0f, 0f)) : IGravity{

    override var acceleration : Vector3f = Vector3f(0f, 0f, 0f)

    override fun applyObjectForce() {
        velocity.add(acceleration)
        acceleration = Vector3f(0f,0f,0f)

        hitBox.translateLocal(velocity)
        hitBox.updateEndPoints()
    }

    override fun getPosition(): Vector3f = hitBox.getPosition()

}