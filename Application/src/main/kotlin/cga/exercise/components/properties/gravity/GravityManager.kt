package cga.exercise.components.properties.gravity

class GravityManager : AbstractGravityManager() {

    override suspend fun applyGravity() {

        for (ob1 in gravityObjects){

            if(ob1.gravityProperty == GravityProperties.adopter || ob1.gravityProperty == GravityProperties.sourceAndAdopter)
                for (ob2 in gravityObjects) {
                    if ((ob2.gravityProperty == GravityProperties.source || ob2.gravityProperty == GravityProperties.sourceAndAdopter) && ob1 !== ob2) {
                        applyGravityTo(ob1, ob2)
                    }
                }
        }

        for (ob1 in gravityObjects){
            ob1.applyObjectForce()
        }
    }
}