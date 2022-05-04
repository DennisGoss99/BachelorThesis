package cga.exercise.components.collision

class TestCollisionBox(override val id : Int, x1 : Float, x2 : Float, y1 : Float, y2 : Float, z1 : Float, z2 : Float ) : ICollisionBox {
    override val minEndPoints = arrayOf(EndPoint(this, x1,true),EndPoint(this,y1,true),EndPoint(this,z1,true))
    override val maxEndPoints = arrayOf(EndPoint(this, x2,false),EndPoint(this,y2,false),EndPoint(this,z2,false))

    override var collided = false
    override var collisionChecked = false

    override var collidedWith = mutableListOf<ICollisionBox>()

    override fun updateEndPoints() {
        TODO("Not yet implemented")
    }
}