package cga.exercise.components.properties.applier

import cga.exercise.components.properties.collision.HitBox
import org.joml.Vector3f
import org.lwjgl.system.CallbackI.I

class Applier {

    var hitBoxes : MutableList<IApplier> = mutableListOf()

    fun clear(){
        hitBoxes.clear()
    }

    fun add(hitBox: IApplier){
        if(!hitBoxes.contains(hitBox))
            hitBoxes.add(hitBox)
    }

    fun setAll(hitBoxes : MutableList<IApplier>){
        this.hitBoxes = hitBoxes
    }

    fun remove(hitBox: IApplier) {
        hitBoxes.remove(hitBox)
    }

    private fun collision(v1 : Vector3f, m1 : Float, v2: Vector3f, m2 : Float) : Vector3f{
        return Vector3f((v1.mul(m1 - m2).add(v2.mul(2 * m2))).div(m1 + m2))
    }

    
    private fun moveBoxOutOfRadius(hitBox : IApplier, hitBox2: IApplier){

        val translate = Vector3f(0f)

        val hitBox1Pos = hitBox.getPosition()
        val hitBox2Pos = hitBox2.getPosition()

        translate.x = if(hitBox1Pos.x >= hitBox2Pos.x)
            -hitBox.minEndPoints[0].value + hitBox2.maxEndPoints[0].value + 0.001f
        else
            -hitBox.maxEndPoints[0].value + hitBox2.minEndPoints[0].value - 0.001f

        translate.y = if(hitBox1Pos.y >= hitBox2Pos.y)
            -hitBox.minEndPoints[1].value + hitBox2.maxEndPoints[1].value + 0.001f
        else
            -hitBox.maxEndPoints[1].value + hitBox2.minEndPoints[1].value - 0.001f

        translate.z = if(hitBox1Pos.z >= hitBox2Pos.z)
            -hitBox.minEndPoints[2].value + hitBox2.maxEndPoints[2].value + 0.001f
        else
            -hitBox.maxEndPoints[2].value + hitBox2.minEndPoints[2].value - 0.001f

        val absTranslate = Vector3f(translate).absolute()


        val minValue = if(absTranslate.x < absTranslate.y) if(absTranslate.x < absTranslate.z) absTranslate.x else absTranslate.z else if(absTranslate.y < absTranslate.z) absTranslate.y else absTranslate.z


        translate.x = if(minValue == absTranslate.x) translate.x else 0f
        translate.y = if(minValue == absTranslate.y) translate.y else 0f
        translate.z = if(minValue == absTranslate.z) translate.z else 0f


        hitBox.translateLocal(translate)

        hitBox.updateEndPoints()
    }

    fun checkBounceOff(){

        val l = hashMapOf<Int, Vector3f>()

        hitBoxes.forEach { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && !l.contains(hitBox.id)) {
                val hitBox2 = hitBox.collidedWith[0] as? IApplier

                if (hitBox2 != null) {

                    moveBoxOutOfRadius(hitBox, hitBox2)
                    l[hitBox.id] = collision(Vector3f(hitBox.velocity), hitBox.mass, Vector3f(hitBox2.velocity), hitBox2.mass)

                    if (hitBox2.interact)
                        l[hitBox2.id] = collision(Vector3f(hitBox2.velocity), hitBox2.mass, Vector3f(hitBox.velocity), hitBox.mass)
                }
            }
        }

        l.forEach {
            hitBoxes[it.key].velocity = it.value
        }

    }

}