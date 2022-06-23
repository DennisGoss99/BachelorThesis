package cga.exercise.game

import cga.framework.printlnTimeMillis
import cga.framework.printlnTimeNano
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.system.measureNanoTime

fun main() {

    runBlocking {

        printlnTimeNano {
            val jobs = mutableListOf<Job>()

            repeat(4){
                jobs.add( GlobalScope.launch {

                })
            }

            jobs.joinAll()
        }

    }




}