package cga.exercise.components.properties.applier

import org.joml.Vector3f

class CollisionHandler : AbstractCollisionHandler() {

    override suspend fun handleCollision(){

        val l = hashMapOf<IApplier, Vector3f>()

        hitBoxes.forEach { it.checked.set(false) }

        hitBoxes.forEach { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && !hitBox.checked.getAndSet(true)) {
                val hitBox2 = hitBox.collidedWith[0]

                if (hitBox2 is IApplier && !hitBox2.checked.getAndSet(true)) {
                    val collisionAxis = getCollisionAxis(hitBox, hitBox2)

                    if (hitBox2.interact){

                        val translate1 = moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis)
                        val translate2 = moveBoxOutOfRadius(hitBox2, hitBox, collisionAxis)

                        hitBox.translateLocal(translate1)
                        hitBox.updateEndPoints()

                        hitBox2.translateLocal(translate2)
                        hitBox2.updateEndPoints()

                        l[hitBox] = getVelocityAfterCollision(hitBox.velocity, getCollisionVector(Vector3f(hitBox.velocity), hitBox.mass, Vector3f(hitBox2.velocity), hitBox2.mass), collisionAxis)
                        l[hitBox2] = getVelocityAfterCollision(hitBox2.velocity, getCollisionVector(Vector3f(hitBox2.velocity), hitBox2.mass, Vector3f(hitBox.velocity), hitBox.mass), collisionAxis)
                    }else{
                        hitBox.translateLocal(moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis))
                        hitBox.updateEndPoints()

                        l[hitBox] = getVelocityAfterCollision(hitBox.velocity, Vector3f(hitBox.velocity).mul(-1f), collisionAxis)
                    }
                }
            }
        }

        l.forEach { entry ->
            entry.key.velocity = entry.value
        }
    }

}