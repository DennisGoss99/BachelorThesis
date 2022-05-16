package cga.exercise.components.collision

import cga.framework.foreachParallel
import cga.framework.foreachParallelIndexed
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class SAP(boxes : MutableList<IHitBox> = mutableListOf()) {

    var idCounter = 0
        get() = field++

    val hitBoxes : MutableList<IHitBox>

    private val endPointsX = mutableListOf<EndPoint>()
    private val endPointsY = mutableListOf<EndPoint>()
    private val endPointsZ = mutableListOf<EndPoint>()

    init {
        this.hitBoxes = boxes
    }

    fun clear(){
        idCounter = 0
        hitBoxes.clear()
        endPointsX.clear()
        endPointsY.clear()
        endPointsZ.clear()
    }

    fun insertBox(hitBox : IHitBox){
        hitBoxes.add(hitBox)
        hitBox.updateEndPoints()

        endPointsX.add(hitBox.minEndPoints[0])
        endPointsX.add(hitBox.maxEndPoints[0])

        endPointsY.add(hitBox.minEndPoints[1])
        endPointsY.add(hitBox.maxEndPoints[1])

        endPointsZ.add(hitBox.minEndPoints[2])
        endPointsZ.add(hitBox.maxEndPoints[2])
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sortParallel(){
        GlobalScope.launch {
            endPointsX.sortBy { it.value }
        }
        GlobalScope.launch {
            endPointsY.sortBy { it.value }
        }
        GlobalScope.launch {
            endPointsZ.sortBy { it.value }
        }
    }

    fun sort(){
        endPointsX.sortBy { it.value }
        endPointsY.sortBy { it.value }
        endPointsZ.sortBy { it.value }
    }

    fun checkCollision(){

        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }

        endPointsX.forEachIndexed { index, endpoint ->
            if (endpoint.isMin) {

                var i = index + 1
                while ( i < endPointsX.size){
                    val endpointNext = endPointsX[i]

                    if (endpointNext.isMin){
                        val collideWith = endpointNext.owner
                        collideWith.collided.set(true)
                        collideWith.collidedWith.add(endpoint.owner)
                        endpoint.owner.collided.set(true)
                        endpoint.owner.collidedWith.add(collideWith)

                    }else
                        if(endpoint.owner.id == endpointNext.owner.id)
                            break

                    i++
                }
            }
        }

        hitBoxes.forEach { hitBox ->
            if(hitBox.collided.get()){
                hitBox.collidedWith.toList().forEach { collideHitBox ->

                    if(!collideHitBox.collisionChecked.get() &&(
                        (hitBox.maxEndPoints[1].value < collideHitBox.minEndPoints[1].value 
						|| hitBox.minEndPoints[1].value > collideHitBox.maxEndPoints[1].value) || 
						(hitBox.maxEndPoints[2].value < collideHitBox.minEndPoints[2].value 
						|| hitBox.minEndPoints[2].value > collideHitBox.maxEndPoints[2].value)
                    ))
                    {
                        if(hitBox.collidedWith.size < 2)
                            hitBox.collided.set(false)

                        if(collideHitBox.collidedWith.size < 2)
                            collideHitBox.collided.set(false)

                        collideHitBox.collidedWith.remove(hitBox)
                        hitBox.collidedWith.remove(collideHitBox)
                    }
                }
                hitBox.collisionChecked.set(true)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun checkCollisionHalfParallel(jobCount : Int){

        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }

        endPointsX.foreachParallelIndexed(jobCount){ endPoint, index ->
            if (endPoint.isMin) {
                var i = index + 1
                while ( i < endPointsX.size){
                    val endpointNext = endPointsX[i]

                    if (endpointNext.isMin){
                        val collideWith = endpointNext.owner
                        collideWith.collided.set(true)
                        collideWith.addCollidedWith(endPoint.owner)
                        endPoint.owner.collided.set(true)
                        endPoint.owner.addCollidedWith(collideWith)

                    }else
                        if(endPoint.owner.id == endpointNext.owner.id)
                            break

                    i++
                }
            }
        }

        hitBoxes.forEach { hitBox ->
            if(hitBox.collided.get()){
                hitBox.collidedWith.toList().forEach { collideHitBox ->

                    if(!collideHitBox.collisionChecked.get() &&(
                                (hitBox.maxEndPoints[1].value < collideHitBox.minEndPoints[1].value || hitBox.minEndPoints[1].value > collideHitBox.maxEndPoints[1].value)
                                        || (hitBox.maxEndPoints[2].value < collideHitBox.minEndPoints[2].value || hitBox.minEndPoints[2].value > collideHitBox.maxEndPoints[2].value)
                                ))
                    {
                        if(hitBox.collidedWith.size < 2)
                            hitBox.collided.set(false)

                        if(collideHitBox.collidedWith.size < 2)
                            collideHitBox.collided.set(false)

                        collideHitBox.collidedWith.remove(hitBox)
                        hitBox.collidedWith.remove(collideHitBox)
                    }
                }
                hitBox.collisionChecked.set(true)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun checkCollisionParallel(jobCount : Int){

        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }

        endPointsX.foreachParallelIndexed(jobCount){ endPoint, index ->
            if (endPoint.isMin) {
                var i = index + 1
                while ( i < endPointsX.size){
                    val endpointNext = endPointsX[i]

                    if (endpointNext.isMin){
                        val collideWith = endpointNext.owner
                        collideWith.collided.set(true)
                        collideWith.addCollidedWith(endPoint.owner)
                        endPoint.owner.collided.set(true)
                        endPoint.owner.addCollidedWith(collideWith)

                    }else
                        if(endPoint.owner.id == endpointNext.owner.id)
                            break

                    i++
                }
            }
        }


        hitBoxes.foreachParallelIndexed(jobCount){ hitBox, _ ->
            if(hitBox.collided.get()){

                hitBox.collidedWith.toList().forEach { collideHitBox : IHitBox ->
                        if(((hitBox.maxEndPoints[1].value < collideHitBox.minEndPoints[1].value || hitBox.minEndPoints[1].value > collideHitBox.maxEndPoints[1].value)
                           || (hitBox.maxEndPoints[2].value < collideHitBox.minEndPoints[2].value || hitBox.minEndPoints[2].value > collideHitBox.maxEndPoints[2].value))
                        ){
                            hitBox.removeCollidedWith(collideHitBox)
                        }
                }

                if (hitBox.collidedWith.size == 0)
                    hitBox.collided.set(false)
            }


        }
    }

    fun collisionCount() = hitBoxes.fold(0){acc, iHitBox -> acc + if (iHitBox.collided.get()) 1 else 0}

}