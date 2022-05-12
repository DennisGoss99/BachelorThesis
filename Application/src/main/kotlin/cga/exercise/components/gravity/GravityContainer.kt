package cga.exercise.components.gravity

import cga.framework.foreachParallel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.math.pow

class GravityContainer(var gravityObjects : MutableList<IGravity> = mutableListOf()) {

    init {
    }

    private fun applyGravityTo(ob1 : IGravity, ob2 : IGravity){
        if (ob1 !== ob2) {

            val ob1Pos = ob1.getPosition()
            val ob2Pos = ob2.getPosition()

            val distance = ob1Pos.distance(ob2Pos)
            val direction = ob2Pos.sub(ob1Pos)

            if(distance > 2f){
                val forceMagnitude = (ob1.mass * ob2.mass) / distance.pow(2)

                ob1.deltaVelocity.add(direction.normalize().mul(forceMagnitude).div(ob1.mass))
            }
        }
    }

    fun add(gravityObject : IGravity) {
        gravityObjects.add(gravityObject)
    }

    fun applyGravityFrom(ob2: IGravity){
        for (ob1 in gravityObjects) {
            applyGravityTo(ob1, ob2)
            ob1.applyObjectForce()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun applyGravityParallelFrom(jobCount : Int, ob2: IGravity){
        gravityObjects.foreachParallel(jobCount){ ob1 ->
            applyGravityTo(ob1, ob2)
            ob1.applyObjectForce()
        }
    }

    fun applyGravityFromAll(){
        for (ob1 in gravityObjects) {
            for (ob2 in gravityObjects) {
                applyGravityTo(ob1, ob2)
            }
            ob1.applyObjectForce()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun applyGravityParallelFromAll(jobCount : Int){
        gravityObjects.foreachParallel(jobCount){ ob1 ->
            for (ob2 in gravityObjects) {
                applyGravityTo(ob1, ob2)
            }
            ob1.applyObjectForce()
        }
    }


}