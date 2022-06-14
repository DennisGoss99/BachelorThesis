package cga.exercise.components.properties.gravity

import cga.framework.foreachParallel
import cga.framework.printlnTimeMillis
import kotlinx.coroutines.DelicateCoroutinesApi

class ParallelGravityManager(jobCount : Int) : AbstractGravityManager() {

    var jobCount = 1
        set(value) {
            if (field < 1)
                throw Exception("jobCount can't be lower then 1")
            field = value
        }

    init {
        this.jobCount = jobCount
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun applyGravity() {

        adopterObjects.foreachParallel(jobCount){ ob1 ->
            for (ob2 in sourceObjects) {
                applyGravityTo(ob1, ob2)
            }
        }

        gravityObjects.foreachParallel(jobCount) { ob1 ->
            ob1.applyObjectForce()
        }
    }

}