package cga.exercise.components.properties.applier

import cga.exercise.components.properties.gravity.GravityHitBox
import org.joml.Vector3f
import kotlin.math.pow
import kotlin.random.Random

class CollisionHandler(removeObject : ((id : Int) -> Unit)? = null, addObject : ((mass : Float, velocity : Vector3f, pos : Vector3f, scale : Vector3f) -> Unit)? = null) : AbstractCollisionHandler(removeObject, addObject) {

    override suspend fun handleCollision(){

        val l = hashMapOf<IApplier, Vector3f>()

        hitBoxes.forEach { it.checked.set(false) }

        hitBoxes.toList().forEach { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && !hitBox.checked.getAndSet(true)) {

                val hitBox2 = hitBox.collidedWith[0]
                if (hitBox2 is IApplier && !hitBox2.checked.getAndSet(true)) {

                    val impact = Vector3f(hitBox.velocity).max(hitBox2.velocity).sub(Vector3f(hitBox.velocity).min(hitBox2.velocity))
                    val maxImpact = if(impact.x > impact.y) if(impact.x > impact.z) impact.x else impact.z else if(impact.y > impact.z) impact.y else impact.z

                    if (hitBox2.interact && maxImpact >= impactScatterValue)
                        scatterObjects(hitBox, hitBox2)
                    else
                        bounceOf(hitBox, hitBox2, l)
                }
            }
        }

        l.forEach { entry ->
            entry.key.velocity = entry.value
        }
    }
}