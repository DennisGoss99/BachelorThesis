package cga.exercise.components.collision

class TestCollisionBox(override val id : Int, x1 : Float, x2 : Float) : ICollisionBox {
    override val minEndPoints = arrayOf(EndPoint(this, x1,true),EndPoint(this,-1f,true),EndPoint(this,-1f,true))
    override val maxEndPoints = arrayOf(EndPoint(this, x2,false),EndPoint(this,-1f,false),EndPoint(this,-1f,false))

    override var collided = false

    override var collidedWith = mutableListOf<ICollisionBox>()

    override fun getCoordinates(): Array<Float> {
        TODO("Not yet implemented")
    }
}