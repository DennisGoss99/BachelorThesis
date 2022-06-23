package cga.exercise.components.properties.collision

import cga.framework.foreachParallelIndexed
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random
import kotlin.system.measureNanoTime

class ParallelSAP(jobCount : Int) : AbstractSAP() {

    var jobCount = 1
        set(value) {
            if (field < 1)
                throw Exception("jobCount can't be lower then 1")
            field = value
        }

    init {
        this.jobCount = jobCount
    }

    var st1 = ""
    var st2 = ""
    var i = 0
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun sort() {
//        val jx = GlobalScope.launch {
//            endPointsX.sortBy { it.value }
//        }
//        val jy = GlobalScope.launch {
//            endPointsY.sortBy { it.value }
//        }
//        val jz = GlobalScope.launch {
//            endPointsZ.sortBy { it.value }
//        }
//
//        jx.join()
//        jy.join()
//        jz.join()



        suspend fun x1(v1 : MutableList<EndPoint>,v2 : MutableList<EndPoint>,v3 : MutableList<EndPoint>) : Long{
            return measureNanoTime {

                val j1x = GlobalScope.launch {
                    v1.sortBy { it.value }
                }
                j1x.join()
                val j1y = GlobalScope.launch {
                    v2.sortBy { it.value }
                }
                j1y.join()
                val j1z = GlobalScope.launch {
                    v3.sortBy { it.value }
                }
                j1z.join()
            }
        }

        suspend fun x2(v1 : MutableList<EndPoint>,v2 : MutableList<EndPoint>,v3 : MutableList<EndPoint>) : Long{
            return measureNanoTime {

                v1.sortBy { it.value }
                v2.sortBy { it.value }
                v3.sortBy { it.value }
            }

        }
        var r1 = 0L
        var r2 = 0L

        if(Random.nextInt(2) == 0) {
            r1 = x1(endPointsX.toMutableList(), endPointsY.toMutableList(), endPointsZ.toMutableList())
            r2 = x2(endPointsX.toMutableList(),endPointsY.toMutableList(),endPointsZ.toMutableList())
        }
        else {
            r2 = x2(endPointsX.toMutableList(),endPointsY.toMutableList(),endPointsZ.toMutableList())
            r1 = x1(endPointsX.toMutableList(), endPointsY.toMutableList(), endPointsZ.toMutableList())
        }

        if(i in 2..100){
            st1 += "$r1;"
            st2 += "$r2;"
        }

        if(i++ == 100) {
            var f = File((endPointsX.count() / 2 - 1).toString()+ ".csv")
            f.writeText(st1 + "\n" + st2)
            println("done")
        }

    }

    override suspend fun checkCollision() {
        hitBoxes.forEach {
            it.collided.set(false)
            it.collisionChecked.set(false)
            it.collidedWith.clear()
        }

        endPointsX.foreachParallelIndexed(jobCount){ endPoint, index ->
            if (endPoint.isMin) {
                var i = index + 1
                while ( i < endPointsX.size){
                    val endpointNext = endPointsX[i]

                    if (endpointNext.isMin){
                        val collideWith = endpointNext.owner
                        collideWith.collided.set(true)
                        collideWith.addCollidedWith(endPoint.owner)
                        endPoint.owner.collided.set(true)
                        endPoint.owner.addCollidedWith(collideWith)

                    }else
                        if(endPoint.owner.id == endpointNext.owner.id)
                            break

                    i++
                }
            }
        }

        hitBoxes.foreachParallelIndexed(jobCount){ hitBox, _ ->
            if(hitBox.collided.get()){

                hitBox.collidedWith.toList().forEach { collideHitBox : IHitBox ->
                    if(((hitBox.maxEndPoints[1].value < collideHitBox.minEndPoints[1].value || hitBox.minEndPoints[1].value > collideHitBox.maxEndPoints[1].value)
                                || (hitBox.maxEndPoints[2].value < collideHitBox.minEndPoints[2].value || hitBox.minEndPoints[2].value > collideHitBox.maxEndPoints[2].value))
                    ){
                        hitBox.removeCollidedWith(collideHitBox)
                    }
                }

                if (hitBox.collidedWith.size == 0)
                    hitBox.collided.set(false)
            }
        }
    }

}