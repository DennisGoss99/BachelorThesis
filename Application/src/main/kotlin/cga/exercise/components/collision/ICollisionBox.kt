package cga.exercise.components.collision

interface ICollisionBox {

    val id : Int

    val minEndPoints : Array<EndPoint>
    val maxEndPoints : Array<EndPoint>

    var collided : Boolean
    var collisionChecked : Boolean
    var collidedWith : MutableList<ICollisionBox>

    fun updateEndPoints()
}