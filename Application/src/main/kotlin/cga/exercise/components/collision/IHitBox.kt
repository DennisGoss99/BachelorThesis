package cga.exercise.components.collision

import cga.exercise.components.geometry.mesh.HitBox
import java.util.concurrent.atomic.AtomicBoolean


interface IHitBox {

    val id : Int

    val minEndPoints : Array<EndPoint>
    val maxEndPoints : Array<EndPoint>

    var collided : AtomicBoolean
    var collisionChecked : Boolean
    var collidedWith : MutableList<IHitBox>

    fun addCollidedWith(hitBox : IHitBox)

    fun updateEndPoints()
}