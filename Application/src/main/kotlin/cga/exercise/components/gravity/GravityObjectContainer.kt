package cga.exercise.components.gravity

import cga.framework.foreachParallel
import kotlinx.coroutines.DelicateCoroutinesApi
import org.joml.Vector3f
import kotlin.math.pow

class GravityObjectContainer{

    private var gravityObjectsApply : MutableList<IGravity> = mutableListOf()
    private var gravityObjectsGetFrom : MutableList<IGravity> = mutableListOf()

    private val gravitationalConstant = 6.674f

    fun add(gravityObject : IGravity, gravityProperties: GravityProperties) {

        when(gravityProperties){
            GravityProperties.source -> gravityObjectsGetFrom.add(gravityObject)

            GravityProperties.adopter -> gravityObjectsApply.add(gravityObject)

            GravityProperties.sourceAndAdopter -> {
                gravityObjectsGetFrom.add(gravityObject)
                gravityObjectsApply.add(gravityObject)
            }
        }

    }

    fun clear(){
        gravityObjectsApply.clear()
        gravityObjectsGetFrom.clear()
    }

    private fun applyGravityTo(ob1 : IGravity, ob2 : IGravity){
        if (ob1 !== ob2) {

            val ob1Pos = ob1.getPosition()
            val ob2Pos = ob2.getPosition()

            val distance = ob1Pos.distance(ob2Pos)
            val direction = ob2Pos.sub(ob1Pos)

            if (distance > 0) {
                val forceMagnitude = gravitationalConstant * (ob1.mass * ob2.mass) / distance.pow(2)

                ob1.acceleration.add(direction.normalize().mul(forceMagnitude).div(ob1.mass).min(Vector3f(1f)))
            }
        }
    }

    fun applyGravity(){
        for (ob1 in gravityObjectsApply)
            for (ob2 in gravityObjectsGetFrom)
                applyGravityTo(ob1, ob2)

        for (ob1 in gravityObjectsApply)
            ob1.applyObjectForce()

    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun applyGravityParallel(jobCount : Int){
        gravityObjectsApply.foreachParallel(jobCount){ ob1 ->
            for (ob2 in gravityObjectsGetFrom) {
                applyGravityTo(ob1, ob2)
            }
        }

        gravityObjectsApply.forEach { ob1 ->
            ob1.applyObjectForce()
        }
    }


}