package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import org.joml.Vector3f

abstract class SpaceObject(val size: Float,
                           val distanceToParent: Vector3f,
                           var speed : Float,
                           var rotationAngle : Float,
                           var selfRotation : Vector3f,
                           material : IMaterial?,
                           orbitAround : Transformable? = null,
                           renderable : RenderableBase) : RenderableBase(renderable.meshes, renderable.modelMatrix, orbitAround), IOrbit {

init {
    if(material != null)
        meshes.forEach { it.material = material }

    translate(distanceToParent)
    scaleLocal(Vector3f(size))

    selfRotation.mul(0.001f)
}


    override fun orbit() {
        val rotation = Vector3f(0f, 0.01f, rotationAngle).normalize()

        rotateAroundPoint(0f ,speed * rotation.y ,0f, Vector3f(0f,0f,0f))
        rotateAroundPoint(0f ,0f, speed * rotation.z, Vector3f(0f,0f,0f))
        rotateLocal( selfRotation.x, selfRotation.y, selfRotation.z)
    }

}