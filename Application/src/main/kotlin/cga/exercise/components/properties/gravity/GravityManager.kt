package cga.exercise.components.properties.gravity

import cga.framework.foreachParallel

class GravityManager : AbstractGravityManager() {

    override suspend fun applyGravity() {

        adopterObjects.forEach{ ob1 ->
            for (ob2 in sourceObjects) {
                applyGravityTo(ob1, ob2)
            }
        }

        gravityObjects.forEach { ob1 ->
            ob1.applyObjectForce()
        }
    }
}