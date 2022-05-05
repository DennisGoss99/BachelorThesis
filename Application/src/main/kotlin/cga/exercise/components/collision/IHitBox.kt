package cga.exercise.components.collision

import java.util.concurrent.atomic.AtomicBoolean


interface IHitBox {

    val id : Int

    val minEndPoints : Array<EndPoint>
    val maxEndPoints : Array<EndPoint>

    var collided : AtomicBoolean
    var collisionChecked : AtomicBoolean
    var collidedWith : MutableList<IHitBox>

    fun addCollidedWith(hitBox : IHitBox)
    fun removeCollidedWith(hitBox : IHitBox)


    fun updateEndPoints()
}