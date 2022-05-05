package cga.exercise.components.collision

import cga.exercise.components.geometry.mesh.HitBox
import java.util.*
import java.util.Collections.synchronizedList
import java.util.concurrent.atomic.AtomicBoolean

class TestHitBox(override val id : Int, x1 : Float, x2 : Float, y1 : Float, y2 : Float, z1 : Float, z2 : Float ) : IHitBox {
    override val minEndPoints = arrayOf(EndPoint(this, x1,true),EndPoint(this,y1,true),EndPoint(this,z1,true))
    override val maxEndPoints = arrayOf(EndPoint(this, x2,false),EndPoint(this,y2,false),EndPoint(this,z2,false))

    override var collided = AtomicBoolean(false)
    override var collisionChecked = false

    override var collidedWith = mutableListOf<IHitBox>()

    @Synchronized
    override fun addCollidedWith(hitBox : IHitBox){
        collidedWith.add(hitBox)
    }


    override fun updateEndPoints() {
    }
}