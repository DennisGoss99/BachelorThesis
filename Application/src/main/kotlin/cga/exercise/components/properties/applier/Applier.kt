package cga.exercise.components.properties.applier

import org.joml.Vector3f

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

    private fun getCollisionVector(v1 : Vector3f, m1 : Float, v2: Vector3f, m2 : Float) : Vector3f{
        return Vector3f((v1.mul(m1 - m2).add(v2.mul(2 * m2))).div(m1 + m2))
    }

    private fun getCollisionAxis(hitBox: IApplier, hitBox2: IApplier) : CollisionAxis{

        val oldHitBox1minPos = Vector3f(hitBox.minEndPoints[0].value, hitBox.minEndPoints[1].value, hitBox.minEndPoints[2].value).sub(hitBox.velocity)
        val oldHitBox1maxPos = Vector3f(hitBox.maxEndPoints[0].value, hitBox.maxEndPoints[1].value, hitBox.maxEndPoints[2].value).sub(hitBox.velocity)
        val oldHitBox2minPos = Vector3f(hitBox2.minEndPoints[0].value, hitBox2.minEndPoints[1].value, hitBox2.minEndPoints[2].value).sub(hitBox2.velocity)
        val oldHitBox2maxPos = Vector3f(hitBox2.maxEndPoints[0].value, hitBox2.maxEndPoints[1].value, hitBox2.maxEndPoints[2].value).sub(hitBox2.velocity)

        val noCollisionX = (oldHitBox1maxPos.x <= oldHitBox2minPos.x || oldHitBox2maxPos.x <= oldHitBox1minPos.x)
        val noCollisionY = (oldHitBox1maxPos.y <= oldHitBox2minPos.y || oldHitBox2maxPos.y <= oldHitBox1minPos.y)
        val noCollisionZ = (oldHitBox1maxPos.z <= oldHitBox2minPos.z || oldHitBox2maxPos.z <= oldHitBox1minPos.z)

        return when{
            noCollisionX && noCollisionY && noCollisionZ -> CollisionAxis.XYZ
            noCollisionX && noCollisionY -> CollisionAxis.XY
            noCollisionX && noCollisionZ -> CollisionAxis.XZ
            noCollisionY && noCollisionZ -> CollisionAxis.YZ
            noCollisionX -> CollisionAxis.X
            noCollisionY -> CollisionAxis.Y
            noCollisionZ -> CollisionAxis.Z
            else -> CollisionAxis.Unknown
        }
    }

    private fun getVelocityAfterCollision(velocity : Vector3f, collisionVelocity : Vector3f, collisionAxis : CollisionAxis) : Vector3f{
        return Vector3f(
        if (collisionAxis == CollisionAxis.X || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.XYZ)
            collisionVelocity.x else velocity.x,
        if (collisionAxis == CollisionAxis.Y || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ)
            collisionVelocity.y else velocity.y,
        if (collisionAxis == CollisionAxis.Z || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ)
            collisionVelocity.z else velocity.z,
        )
    }

    private fun swapZeroOneVectorValues(v : Vector3f) = Vector3f(if(v.x == 0f) 1f else 0f, if(v.y == 0f) 1f else 0f, if(v.z == 0f) 1f else 0f)

    private fun moveBoxOutOfRadiusZeroVelocity(hitBox : IApplier, hitBox2: IApplier) : Vector3f{
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

        return translate
    }

/*    private fun moveBoxOutOfRadius(hitBox : IApplier, hitBox2: IApplier, collisionAxis : CollisionAxis) : Vector3f{
//
//        if(collisionAxis == CollisionAxis.Unknown){
//            println("Collision with unknownAxis")
//            return if (hitBox2.interact)
//                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2).mul(0.5f)
//            else
//                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2)
//        }
//
//
//        if(hitBox.velocity.x == 0f && hitBox.velocity.y == 0f && hitBox.velocity.z == 0f)
//            return if (hitBox2.velocity.x == 0f && hitBox2.velocity.y == 0f && hitBox2.velocity.z == 0f )
//                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2).mul(0.5f)
//            else
//                Vector3f(0f)
//
//        val hitBox1Pos = hitBox.getPosition()
//        val hitBox1Scale = Vector3f((hitBox.maxEndPoints[0].value - hitBox.minEndPoints[0].value) /2f, (hitBox.maxEndPoints[1].value - hitBox.minEndPoints[1].value) /2f, (hitBox.maxEndPoints[2].value - hitBox.minEndPoints[2].value) /2f)
//        val hitBox2Pos = hitBox2.getPosition()
//        val hitBox2Scale = Vector3f((hitBox2.maxEndPoints[0].value - hitBox2.minEndPoints[0].value) /2f, (hitBox2.maxEndPoints[1].value - hitBox2.minEndPoints[1].value) /2f, (hitBox2.maxEndPoints[2].value - hitBox2.minEndPoints[2].value) /2f)
//
//        val factor = Vector3f(0f)
//
//        //collision x-Axis
//        if(hitBox.velocity.x != 0f && (collisionAxis == CollisionAxis.X || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.XYZ )) {
//            factor.x = if (hitBox.velocity.x < 0)
//                ((hitBox2Pos.x + hitBox2Scale.x ) - (hitBox1Pos.x - hitBox1Scale.x)) / hitBox.velocity.x
//            else
//                ((hitBox2Pos.x - hitBox2Scale.x) - (hitBox1Pos.x + hitBox1Scale.x)) / hitBox.velocity.x
//        }
//
//        //collision y-Axis
//        if(hitBox.velocity.y != 0f && (collisionAxis == CollisionAxis.Y || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ )) {
//            factor.y = if (hitBox.velocity.y < 0)
//                ((hitBox2Pos.y + hitBox2Scale.y ) - (hitBox1Pos.y - hitBox1Scale.y)) / hitBox.velocity.y
//            else
//                ((hitBox2Pos.y - hitBox2Scale.y) - (hitBox1Pos.y + hitBox1Scale.y)) / hitBox.velocity.y
//        }
//
//        //collision z-Axis
//        if(hitBox.velocity.z != 0f && (collisionAxis == CollisionAxis.Z || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ )) {
//            factor.z = if (hitBox.velocity.z < 0)
//                ((hitBox2Pos.z + hitBox2Scale.z ) - (hitBox1Pos.z - hitBox1Scale.z)) / hitBox.velocity.z
//            else
//                ((hitBox2Pos.z - hitBox2Scale.z) - (hitBox1Pos.z + hitBox1Scale.z)) / hitBox.velocity.z
//        }
//
//        val absFactor = Vector3f(factor).absolute()
//        val minValue = if(absFactor.x > 0f && (absFactor.x < absFactor.y || absFactor.y <= 0f )) if(absFactor.z > 0f && (absFactor.z < absFactor.x || absFactor.x <= 0f )) absFactor.z else absFactor.x else if(absFactor.y > 0f && (absFactor.y < absFactor.z || absFactor.z <= 0f)) absFactor.y else absFactor.z
//
//        if(minValue != 0f){
//            if (absFactor.x != 0f)
//                absFactor.x = minValue / absFactor.x
//            if (absFactor.y != 0f)
//                absFactor.y = minValue / absFactor.y
//            if (absFactor.z != 0f)
//                absFactor.z = minValue / absFactor.z
//
//            factor.mul(absFactor)
//
//
//            if(!hitBox2.interact)
//                return Vector3f(hitBox.velocity).mul(factor)
//
////             hit1.v / (hit1.v + hit2.v)
//            val translateProportion = Vector3f(0f)
//            val translateProportionDivider = Vector3f(hitBox.velocity).absolute().add(Vector3f(hitBox2.velocity).absolute())
//
//            if(translateProportionDivider.x != 0f)
//                translateProportion.x = hitBox.velocity.x / translateProportionDivider.x
//            if(translateProportionDivider.y != 0f)
//                translateProportion.y = hitBox.velocity.y / translateProportionDivider.y
//            if(translateProportionDivider.z != 0f)
//                translateProportion.z = hitBox.velocity.z / translateProportionDivider.z
//
//            return Vector3f(hitBox.velocity).mul(factor).mul(translateProportion.absolute()).mul(a)
//        }
//
//        return Vector3f(0f)
//    }
*/

    private fun moveBoxOutOfRadius(hitBox : IApplier, hitBox2: IApplier, collisionAxis : CollisionAxis) : Vector3f{

        if(collisionAxis == CollisionAxis.Unknown){
            println("Collision with unknownAxis")
            return if (hitBox2.interact)
                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2).mul(0.5f)
            else
                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2)
        }


        if(hitBox.velocity.x == 0f && hitBox.velocity.y == 0f && hitBox.velocity.z == 0f)
            return if (hitBox2.velocity.x == 0f && hitBox2.velocity.y == 0f && hitBox2.velocity.z == 0f )
                moveBoxOutOfRadiusZeroVelocity(hitBox, hitBox2).mul(0.5f)
            else
                Vector3f(0f)

        val hitBox1Pos = hitBox.getPosition()
        val hitBox1Scale = Vector3f((hitBox.maxEndPoints[0].value - hitBox.minEndPoints[0].value) /2f, (hitBox.maxEndPoints[1].value - hitBox.minEndPoints[1].value) /2f, (hitBox.maxEndPoints[2].value - hitBox.minEndPoints[2].value) /2f)
        val hitBox2Pos = hitBox2.getPosition()
        val hitBox2Scale = Vector3f((hitBox2.maxEndPoints[0].value - hitBox2.minEndPoints[0].value) /2f, (hitBox2.maxEndPoints[1].value - hitBox2.minEndPoints[1].value) /2f, (hitBox2.maxEndPoints[2].value - hitBox2.minEndPoints[2].value) /2f)

        val factor = Vector3f(0f)

        //collision x-Axis
        if(hitBox.velocity.x != 0f && (collisionAxis == CollisionAxis.X || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.XYZ )) {
            factor.x = if (hitBox.velocity.x < hitBox2.velocity.x)
                ((hitBox2Pos.x + hitBox2Scale.x ) - (hitBox1Pos.x - hitBox1Scale.x)) / hitBox.velocity.x
            else
                ((hitBox2Pos.x - hitBox2Scale.x) - (hitBox1Pos.x + hitBox1Scale.x)) / hitBox.velocity.x
        }

        //collision y-Axis
        if(hitBox.velocity.y != 0f && (collisionAxis == CollisionAxis.Y || collisionAxis == CollisionAxis.XY || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ )) {
            factor.y = if (hitBox.velocity.y < hitBox2.velocity.y)
                ((hitBox2Pos.y + hitBox2Scale.y ) - (hitBox1Pos.y - hitBox1Scale.y)) / hitBox.velocity.y
            else
                ((hitBox2Pos.y - hitBox2Scale.y) - (hitBox1Pos.y + hitBox1Scale.y)) / hitBox.velocity.y
        }

        //collision z-Axis
        if(hitBox.velocity.z != 0f && (collisionAxis == CollisionAxis.Z || collisionAxis == CollisionAxis.XZ || collisionAxis == CollisionAxis.YZ || collisionAxis == CollisionAxis.XYZ )) {
            factor.z = if (hitBox.velocity.z < hitBox2.velocity.z)
                ((hitBox2Pos.z + hitBox2Scale.z ) - (hitBox1Pos.z - hitBox1Scale.z)) / hitBox.velocity.z
            else
                ((hitBox2Pos.z - hitBox2Scale.z) - (hitBox1Pos.z + hitBox1Scale.z)) / hitBox.velocity.z
        }

        val absFactor = Vector3f(factor).absolute()
        val minValue = if(absFactor.x > 0f && (absFactor.x < absFactor.y || absFactor.y <= 0f )) if(absFactor.z > 0f && (absFactor.z < absFactor.x || absFactor.x <= 0f )) absFactor.z else absFactor.x else if(absFactor.y > 0f && (absFactor.y < absFactor.z || absFactor.z <= 0f)) absFactor.y else absFactor.z

        if(minValue != 0f){
            if (absFactor.x != 0f)
                absFactor.x = minValue / absFactor.x
            if (absFactor.y != 0f)
                absFactor.y = minValue / absFactor.y
            if (absFactor.z != 0f)
                absFactor.z = minValue / absFactor.z

            factor.mul(absFactor)


            if(!hitBox2.interact)
                return Vector3f(hitBox.velocity).mul(factor)

//             hit1.v / (hit1.v + hit2.v)
            val translateProportion = Vector3f(0f)
            val translateProportionDivider = Vector3f(hitBox.velocity).absolute().add(Vector3f(hitBox2.velocity).absolute())

            if(translateProportionDivider.x != 0f)
                translateProportion.x = hitBox.velocity.x / translateProportionDivider.x
            if(translateProportionDivider.y != 0f)
                translateProportion.y = hitBox.velocity.y / translateProportionDivider.y
            if(translateProportionDivider.z != 0f)
                translateProportion.z = hitBox.velocity.z / translateProportionDivider.z


            return Vector3f(hitBox.velocity).mul(factor).mul(translateProportion.absolute())
        }

        return Vector3f(0f)
    }

    fun checkBounceOff(){

        val l = hashMapOf<Int, Vector3f>()

        hitBoxes.forEach { hitBox ->
            if (hitBox.interact && hitBox.collided.get() && !l.contains(hitBox.id)) {
                val hitBox2 = hitBox.collidedWith[0] as? IApplier

                if (hitBox2 != null) {
                    val collisionAxis = getCollisionAxis(hitBox, hitBox2)

                    if (hitBox2.interact){

                        val translate1 = moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis)
                        val translate2 = moveBoxOutOfRadius(hitBox2, hitBox, collisionAxis)

                        hitBox.translateLocal(translate1)
                        hitBox.updateEndPoints()

                        hitBox2.translateLocal(translate2)
                        hitBox2.updateEndPoints()

                        l[hitBox.id] = getVelocityAfterCollision(hitBox.velocity, getCollisionVector(Vector3f(hitBox.velocity), hitBox.mass, Vector3f(hitBox2.velocity), hitBox2.mass), collisionAxis)
                        l[hitBox2.id] = getVelocityAfterCollision(hitBox2.velocity, getCollisionVector(Vector3f(hitBox2.velocity), hitBox2.mass, Vector3f(hitBox.velocity), hitBox.mass), collisionAxis)
                    }else{
                        hitBox.translateLocal(moveBoxOutOfRadius(hitBox, hitBox2, collisionAxis))
                        hitBox.updateEndPoints()

                        l[hitBox.id] = getVelocityAfterCollision(hitBox.velocity, Vector3f(hitBox.velocity).mul(-1f), collisionAxis)
                    }
                }
            }
        }

        l.forEach { entry ->
            hitBoxes.first { it.id == entry.key }.velocity = entry.value
        }

    }

}