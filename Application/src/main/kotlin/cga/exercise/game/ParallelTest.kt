package cga.exercise.game

import cga.framework.printlnTimeMillis
import cga.framework.printlnTimeNano
import kotlinx.coroutines.*
import kotlin.concurrent.timer
import kotlin.random.Random

import kotlin.system.measureNanoTime

fun main() {

    runBlocking {
        repeat(10){

            var i = 23123L
            val l = MutableList<Int>(1000000){ Random(i++).nextInt(0,10000000)}
            i = 23123L
            val l2 = MutableList<Int>(1000000){Random(i++).nextInt(0,10000000)}
//
//
//            printlnTimeNano {
//
//                val jobs = mutableListOf<Job>()
//
//                repeat(1){
//                    jobs.add( GlobalScope.launch {
//                        l.sort()
//                    })
//                }
//
//                jobs.joinAll()
//            }

            printlnTimeNano {
                l2.sort()
            }

        }


//        printlnTimeNano {
//            val jobs = mutableListOf<Job>()
//            repeat(1){
//                jobs.add( GlobalScope.launch {
//                })
//            }
//
//            jobs.joinAll()
//        }

    }
}