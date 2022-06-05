package cga.exercise.components.properties.collision

class SAP : AbstractSAP() {

    override suspend fun sort(){
        endPointsX.sortBy { it.value }
        endPointsY.sortBy { it.value }
        endPointsZ.sortBy { it.value }
    }

    override suspend fun checkCollision(){

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

}