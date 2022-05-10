package cga.exercise.components.gravity

import cga.exercise.components.collision.HitBox
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class GravityContainer(var gravityObjects : MutableList<IGravity> = mutableListOf()) {




    init {




    }

    fun add(gravityObject : IGravity) {
        gravityObjects.add(gravityObject)
    }

    fun applyGravity(){

        for (ob1 in gravityObjects) {
            for (ob2 in gravityObjects) {
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
        }

        for (ob1 in gravityObjects) {
            ob1.applyObjectForce()
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun applyGravityParallel(jobCount : Int){

        val jobs = mutableListOf<Job>()

        val chunkSize = gravityObjects.size / jobCount
        val remains = gravityObjects.size - (chunkSize * jobCount)

        for(jobIndex in 0 until jobCount){
            jobs.add(GlobalScope.launch {
                for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                    val ob1 = gravityObjects[index]
                        for (ob2 in gravityObjects) {
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
                    }
            })
        }


        for (ob1 in gravityObjects) {
            ob1.applyObjectForce()
        }
    }


    fun moveObjects() {
        for (ob1 in gravityObjects) {
            ob1.move()
        }
    }


}