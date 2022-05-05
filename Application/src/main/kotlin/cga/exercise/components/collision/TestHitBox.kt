package cga.exercise.components.collision

import java.util.concurrent.atomic.AtomicBoolean

class TestHitBox(override val id : Int, x1 : Float, x2 : Float, y1 : Float, y2 : Float, z1 : Float, z2 : Float ) : IHitBox {
    override val minEndPoints = arrayOf(EndPoint(this, x1,true),EndPoint(this,y1,true),EndPoint(this,z1,true))
    override val maxEndPoints = arrayOf(EndPoint(this, x2,false),EndPoint(this,y2,false),EndPoint(this,z2,false))

    override var collided = AtomicBoolean(false)
    override var collisionChecked = AtomicBoolean(false)

    override var collidedWith = mutableListOf<IHitBox>()

    @Synchronized
    override fun addCollidedWith(hitBox : IHitBox){
        collidedWith.add(hitBox)
    }

    @Synchronized
    override fun removeCollidedWith(hitBox: IHitBox) {
        collidedWith.remove(hitBox)
    }


    override fun updateEndPoints() {
    }
}