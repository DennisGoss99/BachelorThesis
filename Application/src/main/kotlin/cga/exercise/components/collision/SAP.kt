package cga.exercise.components.collision

import kotlinx.coroutines.*

class SAP(boxes : MutableList<ICollisionBox> = mutableListOf()) {

    var idCounter = 0
        get() = field++

    private val boxes : MutableList<ICollisionBox>

    private val endPointsX = mutableListOf<EndPoint>()
    private val endPointsY = mutableListOf<EndPoint>()
    private val endPointsZ = mutableListOf<EndPoint>()

    init {
        this.boxes = boxes
    }

    fun insertBox(box : ICollisionBox){
        boxes.add(box)

        endPointsX.add(box.minEndPoints[0])
        endPointsX.add(box.maxEndPoints[0])

        endPointsY.add(box.minEndPoints[1])
        endPointsY.add(box.maxEndPoints[1])

        endPointsZ.add(box.minEndPoints[2])
        endPointsZ.add(box.maxEndPoints[2])
    }

    fun sort(){
        endPointsX.sortBy { it.value }
        endPointsY.sortBy { it.value }
        endPointsZ.sortBy { it.value }
    }

    fun checkCollision(){

        boxes.forEach {
            it.collided = false
            it.collidedWith.clear()
        }


        endPointsX.forEachIndexed { index, endpoint ->
            if (endpoint.isMin) {

                var i = index + 1
                while ( i < endPointsX.size){
                    val endpointNext = endPointsX[i]

                    if (endpointNext.isMin){
                        val collideWith = endpointNext.owner
                        collideWith.collided = true
                        collideWith.collidedWith.add(endpoint.owner)
                        endpoint.owner.collided = true
                        endpoint.owner.collidedWith.add(collideWith)

                    }else
                        if(endpoint.owner.id == endpointNext.owner.id)
                            break

                    i++
                }
            }
        }
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    suspend fun checkCollision2(){
//
//        boxes.forEach {
//            it.collided = false
//            it.collidedWith.clear()
//        }
//        val jobs = mutableListOf<Job>()
//        jobs.add(GlobalScope.launch {
//            for(i2 in 0/3 * boxes.size  until boxes.size * 1/3){
//                val box = boxes[i2]
//
//                val minIndex = box.minEndPoints[0]
//                val maxIndex = box.maxEndPoints[0]
//
//                for(i in minIndex + 1 until maxIndex){
//                    if(endPointsX[i].isMin) {
//                        val collideWith = endPointsX[i].owner
//                        collideWith.collided = true
//                        collideWith.collidedWith.add(box)
//                        box.collided = true
//                        box.collidedWith.add(collideWith)
//                    }
//                }
//            }
//        })
//
//        jobs.add( GlobalScope.launch {
//            for(i2 in boxes.size * 1/3 until boxes.size * 2/3){
//                val box = boxes[i2]
//
//                val minIndex = box.minEndPoints[0]
//                val maxIndex = box.maxEndPoints[0]
//
//                for(i in minIndex + 1 until maxIndex){
//                    if(endPointsX[i].isMin) {
//                        val collideWith = endPointsX[i].owner
//                        collideWith.collided = true
//                        collideWith.collidedWith.add(box)
//                        box.collided = true
//                        box.collidedWith.add(collideWith)
//                    }
//                }
//            }
//        }
//        )
//
//        jobs.add( GlobalScope.launch {
//            for(i2 in boxes.size * 2/3 until boxes.size * 3/3){
//                val box = boxes[i2]
//
//                val minIndex = box.minEndPoints[0]
//                val maxIndex = box.maxEndPoints[0]
//
//                for(i in minIndex + 1 until maxIndex){
//                    if(endPointsX[i].isMin) {
//                        val collideWith = endPointsX[i].owner
//                        collideWith.collided = true
//                        collideWith.collidedWith.add(box)
//                        box.collided = true
//                        box.collidedWith.add(collideWith)
//                    }
//                }
//            }
//        }
//        )
//
//        jobs.joinAll()
//
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    suspend fun checkCollision3(){
//        boxes.forEach {
//            it.collided = false
//            it.collidedWith.clear()
//        }
//
//        val jobs = mutableListOf<Job>()
//
//        boxes.forEachIndexed { index, box ->
//            jobs.add(GlobalScope.launch {
//
//                val minIndex = box.minEndPoints[0]
//                val maxIndex = box.maxEndPoints[0]
//
//                for (i in minIndex + 1 until maxIndex) {
//                    if (endPointsX[i].isMin) {
//                        val collideWith = endPointsX[i].owner
//                        collideWith.collided = true
//                        collideWith.collidedWith.add(box)
//                        box.collided = true
//                        box.collidedWith.add(collideWith)
//                    }
//                }
//            })
//        }
//
//        jobs.joinAll()
//    }

}