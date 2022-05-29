package cga.exercise.components.collision

import cga.framework.foreachParallelIndexed
import kotlinx.coroutines.*


abstract class AbstractSAP{

    var idCounter = 0
        get() = field++

    val hitBoxes : MutableList<IHitBox> = mutableListOf()

    protected val endPointsX = mutableListOf<EndPoint>()
    protected val endPointsY = mutableListOf<EndPoint>()
    protected val endPointsZ = mutableListOf<EndPoint>()

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

    fun remove(hitBox : IHitBox){
        hitBoxes.remove(hitBox)
        endPointsX.removeAll{ it.owner.id == hitBox.id }
        endPointsY.removeAll{ it.owner.id == hitBox.id }
        endPointsZ.removeAll{ it.owner.id == hitBox.id }
    }

    abstract suspend fun sort()

    abstract suspend fun checkCollision()

    fun collisionCount() = hitBoxes.fold(0){acc, iHitBox -> acc + if (iHitBox.collided.get()) 1 else 0}

}