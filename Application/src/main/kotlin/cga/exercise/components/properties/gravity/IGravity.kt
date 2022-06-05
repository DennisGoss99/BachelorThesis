package cga.exercise.components.properties.gravity

import org.joml.Vector3f

interface IGravity {

    val id : Int

    var mass : Float
    var velocity : Vector3f
    var acceleration : Vector3f
    var gravityProperty : GravityProperties

    fun applyObjectForce()
    fun getPosition() : Vector3f
}