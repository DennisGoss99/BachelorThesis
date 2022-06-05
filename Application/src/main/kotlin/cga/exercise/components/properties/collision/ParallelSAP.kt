package cga.exercise.components.properties.collision

import cga.framework.foreachParallelIndexed
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ParallelSAP(jobCount : Int) : AbstractSAP() {

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
    override suspend fun sort() {
        val jx = GlobalScope.launch {
            endPointsX.sortBy { it.value }
        }
        val jy = GlobalScope.launch {
            endPointsY.sortBy { it.value }
        }
        val jz = GlobalScope.launch {
            endPointsZ.sortBy { it.value }
        }

        jx.join()
        jy.join()
        jz.join()
    }

    override suspend fun checkCollision() {
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

}