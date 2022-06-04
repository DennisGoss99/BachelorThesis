package cga.exercise.components.gravity

import cga.exercise.components.collision.HitBox
import org.joml.Vector3f

class GravityHitBox(id : Int, override var mass: Float, pos : Vector3f = Vector3f(0f), scale : Vector3f = Vector3f(1f), override var velocity : Vector3f = Vector3f(0f, 0f, 0f)) : HitBox(id, pos, scale), IGravity{

    override var acceleration : Vector3f = Vector3f(0f, 0f, 0f)

    override fun applyObjectForce() {
        velocity.add(acceleration)
        acceleration = Vector3f(0f,0f,0f)

        translateLocal(velocity)
        updateEndPoints()
    }

}