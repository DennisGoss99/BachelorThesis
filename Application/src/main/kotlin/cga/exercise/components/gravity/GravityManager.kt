package cga.exercise.components.gravity

class GravityManager : AbstractGravityManager() {

    override suspend fun applyGravity() {
        for (ob1 in gravityObjectsApply)
            for (ob2 in gravityObjectsGetFrom)
                applyGravityTo(ob1, ob2)

        for (ob1 in gravityObjectsApply)
            ob1.applyObjectForce()

    }
}