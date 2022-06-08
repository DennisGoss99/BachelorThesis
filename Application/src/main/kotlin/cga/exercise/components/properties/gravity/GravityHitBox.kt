package cga.exercise.components.properties.gravity

import cga.exercise.components.properties.applier.IApplier
import cga.exercise.components.properties.collision.HitBox
import org.joml.Vector3f

class GravityHitBox(id : Int,
                    override var mass: Float,
                    override var gravityProperty:
                    GravityProperties,
                    pos : Vector3f = Vector3f(0f),
                    scale : Vector3f = Vector3f(1f),
                    override var velocity : Vector3f = Vector3f(0f, 0f, 0f),
                    override var interact : Boolean = true) : HitBox(id, pos, scale), IApplier {

    override var acceleration : Vector3f = Vector3f(0f, 0f, 0f)

    override fun applyObjectForce() {
        velocity.add(acceleration)
        acceleration.set(0f)

        translate(velocity)
        updateEndPoints()
    }

}