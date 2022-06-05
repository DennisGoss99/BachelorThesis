package cga.exercise.components.properties.gravity

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

        gravityObjects.foreachParallel(jobCount){ ob1 ->

            if(ob1.gravityProperty == GravityProperties.adopter || ob1.gravityProperty == GravityProperties.sourceAndAdopter)
                for (ob2 in gravityObjects) {
                    if ((ob2.gravityProperty == GravityProperties.source || ob2.gravityProperty == GravityProperties.sourceAndAdopter) && ob1 !== ob2) {
                        applyGravityTo(ob1, ob2)
                    }
                }

            ob1.applyObjectForce()
        }
    }

}