package cga.exercise.components.properties.applier

import cga.exercise.components.properties.collision.HitBox
import cga.framework.foreachParallel
import cga.framework.printlnTimeMillis
import kotlinx.coroutines.*
import org.joml.Vector3f

class ParallelCollisionHandler(jobCount : Int) : AbstractCollisionHandler() {

    var jobCount = 1
        set(value) {
            if (field < 1)
                throw Exception("jobCount can't be lower then 1")
            field = value
        }

    init {
        this.jobCount = jobCount
    }


    override suspend fun handleCollision() {

        val l = hashMapOf<IApplier, Vector3f>()

        @Synchronized
        fun setHashmap (hitBox : IApplier, velocity: Vector3f){
            l[hitBox] = velocity
        }

        hitBoxes.forEach { it.checked.set(false) }

        hitBoxes.foreachParallel(jobCount) { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && !hitBox.checked.getAndSet(true)) {

                val hitBox2 = hitBox.collidedWith[0]
                if (hitBox2 is IApplier && !(hitBox2.checked.getAndSet(true))) {

                    val collisionAxis = getCollisionAxis(hitBox, hitBox2)

                    if (hitBox2.interact) {

                        val translate1 = moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis)
                        val translate2 = moveBoxOutOfRadius(hitBox2, hitBox, collisionAxis)

                        hitBox.translateLocal(translate1)
                        hitBox.updateEndPoints()

                        hitBox2.translateLocal(translate2)
                        hitBox2.updateEndPoints()

                        setHashmap(hitBox, getVelocityAfterCollision(hitBox.velocity, getCollisionVector(Vector3f(hitBox.velocity), hitBox.mass, Vector3f(hitBox2.velocity), hitBox2.mass), collisionAxis))
                        setHashmap(hitBox2, getVelocityAfterCollision(hitBox2.velocity, getCollisionVector(Vector3f(hitBox2.velocity), hitBox2.mass, Vector3f(hitBox.velocity), hitBox.mass), collisionAxis))
                    } else {
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




//First attempt
/*    override suspend fun handleCollision() {
        val collidedApplier = mutableMapOf<Int, IApplier>()

        @Synchronized
        fun addCollidedApplier(applier: IApplier, applier2: IApplier) {
            if (collidedApplier.contains(applier.id) || collidedApplier.contains(applier2.id))
                return
            collidedApplier[applier.id] = applier
        }

        hitBoxes.foreachParallel(jobCount) { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && hitBox.collidedWith[0] is IApplier)
                addCollidedApplier(hitBox, hitBox.collidedWith[0] as IApplier)
        }


        val l = hashMapOf<Int, Vector3f>()

        collidedApplier.values.forEach { hitBox ->
            val hitBox2 = hitBox.collidedWith[0] as IApplier

            val collisionAxis = getCollisionAxis(hitBox, hitBox2)

            if (hitBox2.interact){

                val translate1 = moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis)
                val translate2 = moveBoxOutOfRadius(hitBox2, hitBox, collisionAxis)

                hitBox.translateLocal(translate1)
                hitBox.updateEndPoints()

                hitBox2.translateLocal(translate2)
                hitBox2.updateEndPoints()

                l[hitBox.id] = getVelocityAfterCollision(hitBox.velocity, getCollisionVector(Vector3f(hitBox.velocity), hitBox.mass, Vector3f(hitBox2.velocity), hitBox2.mass), collisionAxis)
                l[hitBox2.id] = getVelocityAfterCollision(hitBox2.velocity, getCollisionVector(Vector3f(hitBox2.velocity), hitBox2.mass, Vector3f(hitBox.velocity), hitBox.mass), collisionAxis)
            }else{
                hitBox.translateLocal(moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis))
                hitBox.updateEndPoints()

                l[hitBox.id] = getVelocityAfterCollision(hitBox.velocity, Vector3f(hitBox.velocity).mul(-1f), collisionAxis)
            }
        }

        l.forEach { entry ->
            hitBoxes.first { it.id == entry.key }.velocity = entry.value
        }
    }
*/

}
