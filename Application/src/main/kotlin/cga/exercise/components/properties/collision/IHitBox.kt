package cga.exercise.components.properties.collision

import org.joml.Vector3f
import java.util.concurrent.atomic.AtomicBoolean


interface IHitBox {

    companion object{
        var idCounter = 0
            get() = field++
    }

    val id : Int

    val minEndPoints : Array<EndPoint>
    val maxEndPoints : Array<EndPoint>

    var collided : AtomicBoolean
    var collisionChecked : AtomicBoolean
    var collidedWith : MutableList<IHitBox>

    fun addCollidedWith(hitBox : IHitBox)
    fun removeCollidedWith(hitBox : IHitBox)

    fun updateEndPoints()
    fun translateLocal(vec : Vector3f)

}