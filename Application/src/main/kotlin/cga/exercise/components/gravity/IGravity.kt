package cga.exercise.components.gravity

import org.joml.Vector3f

interface IGravity {

    var mass : Float
    var velocity : Vector3f
    var acceleration : Vector3f

    fun applyObjectForce()
    fun getPosition() : Vector3f
}