package cga.exercise.components.properties.gravity

import cga.framework.foreachParallel
import kotlinx.coroutines.DelicateCoroutinesApi
import org.joml.Vector3f
import kotlin.math.pow

 abstract class AbstractGravityManager{

    protected var gravityObjects : MutableList<IGravity> = mutableListOf()

    private val gravitationalConstant = 6.674f

    fun add(gravityObject : IGravity) {
        if(!gravityObjects.contains(gravityObject))
            gravityObjects.add(gravityObject)
    }

    fun setAll(gravityObjects : MutableList<IGravity>){
        this.gravityObjects = gravityObjects
    }

    fun remove(gravityObject : IGravity){
        gravityObjects.remove(gravityObject)
    }
    fun remove(id : Int){
        gravityObjects.removeAll{it.id == id}
    }

    fun clear(){
        gravityObjects.clear()
    }

    protected fun applyGravityTo(ob1 : IGravity, ob2 : IGravity){

        val ob1Pos = ob1.getPosition()
        val ob2Pos = ob2.getPosition()

        val distance = ob1Pos.distance(ob2Pos)
        val direction = ob2Pos.sub(ob1Pos)

        if (distance > 0) {
            val forceMagnitude = gravitationalConstant * ( /* ob1.mass * */ ob2.mass) / distance.pow(2)

            ob1.acceleration.add(direction.normalize().mul(forceMagnitude)/*.div(ob1.mass)*/.min(Vector3f(1f)))
        }
    }

    abstract suspend fun applyGravity()

}