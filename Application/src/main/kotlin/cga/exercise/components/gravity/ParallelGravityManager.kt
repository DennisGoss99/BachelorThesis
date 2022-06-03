package cga.exercise.components.gravity

import cga.framework.foreachParallel
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
        gravityObjectsApply.foreachParallel(jobCount){ ob1 ->
            for (ob2 in gravityObjectsGetFrom) {
                applyGravityTo(ob1, ob2)
            }
        }

        for (ob1 in gravityObjectsApply)
            ob1.applyObjectForce()
    }

}