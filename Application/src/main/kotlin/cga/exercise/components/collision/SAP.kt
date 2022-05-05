package cga.exercise.components.collision

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
        println("c: ${hitBoxes.count(){it.collided.get()}}")
    }


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun checkCollision2(jobCount : Int){

        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }
        val jobs = mutableListOf<Job>()

        val chunkSize = endPointsX.size / jobCount
        val remains = endPointsX.size - (chunkSize * jobCount)

        for(jobIndex in 0 until jobCount){
            jobs.add(GlobalScope.launch {
                for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                    val endpoint = endPointsX[index]
                    if (endpoint.isMin) {
                        var i = index + 1
                        while ( i < endPointsX.size){
                            val endpointNext = endPointsX[i]

                            if (endpointNext.isMin){
                                val collideWith = endpointNext.owner
                                collideWith.collided.set(true)
                                collideWith.addCollidedWith(endpoint.owner)
                                endpoint.owner.collided.set(true)
                                endpoint.owner.addCollidedWith(collideWith)

                            }else
                                if(endpoint.owner.id == endpointNext.owner.id)
                                    break

                            i++
                        }
                    }
                }
            })
        }

        jobs.joinAll()

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
//        println("c: ${hitBoxes.count(){it.collided.get()}}")
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun checkCollision3(jobCount : Int){

        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }
        val jobs = mutableListOf<Job>()

        val chunkSize = endPointsX.size / jobCount
        val remains = endPointsX.size - (chunkSize * jobCount)

        for(jobIndex in 0 until jobCount){
            jobs.add(GlobalScope.launch {
                for(index in jobIndex * chunkSize until (jobIndex + 1) * chunkSize + if(jobIndex != jobCount-1) 0 else remains){
                    val endpoint = endPointsX[index]
                    if (endpoint.isMin) {
                        var i = index + 1
                        while ( i < endPointsX.size){
                            val endpointNext = endPointsX[i]

                            if (endpointNext.isMin){
                                val collideWith = endpointNext.owner
                                collideWith.collided.set(true)
                                collideWith.addCollidedWith(endpoint.owner)
                                endpoint.owner.collided.set(true)
                                endpoint.owner.addCollidedWith(collideWith)

                            }else
                                if(endpoint.owner.id == endpointNext.owner.id)
                                    break

                            i++
                        }
                    }
                }
            })
        }
        jobs.joinAll()

        val jobs2 = mutableListOf<Job>()
        val chunkSize2 = hitBoxes.size / jobCount
        val remains2 = hitBoxes.size - (chunkSize2 * jobCount)

        for(jobIndex2 in 0 until jobCount){
            jobs2.add(GlobalScope.launch {
                for(index2 in jobIndex2 * chunkSize2 until (jobIndex2 + 1) * chunkSize2 + if(jobIndex2 != jobCount - 1) 0 else remains2){
                    val hitBox = hitBoxes[index2]
                    if(hitBox.collided.get()){
                        hitBox.collidedWith.toList().forEach { collideHitBox : IHitBox ->
                                if(//collideHitBox != null &&// !collideHitBox.collisionChecked.get()&&
                                     ((hitBox.maxEndPoints[1].value < collideHitBox.minEndPoints[1].value || hitBox.minEndPoints[1].value > collideHitBox.maxEndPoints[1].value)
                                                    || (hitBox.maxEndPoints[2].value < collideHitBox.minEndPoints[2].value || hitBox.minEndPoints[2].value > collideHitBox.maxEndPoints[2].value))
                                ){


//                                    if (collideHitBox.collidedWith.size < 2)
//                                        collideHitBox.collided.set(false)


                                    hitBox.removeCollidedWith(collideHitBox)
//                                    collideHitBox?.removeCollidedWith(hitBox)
                                }
                        }

                        if (hitBox.collidedWith.size == 0)
                            hitBox.collided.set(false)

                        //hitBox.collisionChecked.set(true)
                    }
                }
            })
        }
        jobs2.joinAll()
//        println("c: ${hitBoxes.count(){it.collided.get()}}")
    }

}