package cga.exercise.components.collision


abstract class AbstractSAP{

    var idCounter = 0
        get() = field++

    var hitBoxes : MutableList<IHitBox> = mutableListOf()

    protected var endPointsX = mutableListOf<EndPoint>()
    protected var endPointsY = mutableListOf<EndPoint>()
    protected var endPointsZ = mutableListOf<EndPoint>()

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

    fun setAllBoxes(hitBoxes: MutableList<IHitBox>){
        this.hitBoxes = hitBoxes

        endPointsX = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].minEndPoints[0].value,true)
            else
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].maxEndPoints[0].value,false)
        }

        endPointsY = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].minEndPoints[1].value,true)
            else
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].maxEndPoints[1].value,false)
        }

        endPointsZ = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].minEndPoints[2].value,true)
            else
                EndPoint(hitBoxes[it / 2], hitBoxes[it / 2].maxEndPoints[2].value,false)
        }
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