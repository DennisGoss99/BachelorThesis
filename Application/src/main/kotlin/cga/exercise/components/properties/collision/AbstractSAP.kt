package cga.exercise.components.properties.collision


abstract class AbstractSAP{

    var hitBoxes : MutableList<IHitBox> = mutableListOf()

    protected var endPointsX : MutableList<EndPoint> = mutableListOf()
    protected var endPointsY : MutableList<EndPoint> = mutableListOf()
    protected var endPointsZ : MutableList<EndPoint> = mutableListOf()

    fun clear(){
        hitBoxes.clear()
        endPointsX.clear()
        endPointsY.clear()
        endPointsZ.clear()
    }

    fun insertBox(hitBox : IHitBox){
        if(!hitBoxes.contains(hitBox))
            hitBoxes.add(hitBox)
        hitBox.updateEndPoints()

        endPointsX.add(hitBox.minEndPoints[0])
        endPointsX.add(hitBox.maxEndPoints[0])

        endPointsY.add(hitBox.minEndPoints[1])
        endPointsY.add(hitBox.maxEndPoints[1])

        endPointsZ.add(hitBox.minEndPoints[2])
        endPointsZ.add(hitBox.maxEndPoints[2])
    }

    fun setAll(hitBoxes: MutableList<IHitBox>){
        this.hitBoxes = hitBoxes

        endPointsX = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                hitBoxes[it / 2].minEndPoints[0]
            else
                hitBoxes[it / 2].maxEndPoints[0]
        }

        endPointsY = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                hitBoxes[it / 2].minEndPoints[1]
            else
                hitBoxes[it / 2].maxEndPoints[1]
        }

        endPointsZ = MutableList(hitBoxes.size * 2){
            if(it % 2 == 0)
                hitBoxes[it / 2].minEndPoints[2]
            else
                hitBoxes[it / 2].maxEndPoints[2]
        }
    }

    fun remove(hitBox : IHitBox){
        hitBoxes.remove(hitBox)
        endPointsX.removeAll{ it.owner.id == hitBox.id }
        endPointsY.removeAll{ it.owner.id == hitBox.id }
        endPointsZ.removeAll{ it.owner.id == hitBox.id }
    }

    fun remove(id : Int){
        endPointsX.removeAll{ it.owner.id == id }
        endPointsY.removeAll{ it.owner.id == id }
        endPointsZ.removeAll{ it.owner.id == id }
        hitBoxes.removeAll { it.id == id }
    }

    fun collisionCount() = hitBoxes.fold(0){acc, iHitBox -> acc + if (iHitBox.collided.get()) 1 else 0}

    abstract suspend fun sort()

    abstract suspend fun checkCollision()


}