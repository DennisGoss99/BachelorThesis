package cga.exercise

import cga.exercise.components.text.FontType
import cga.exercise.game.Game
import cga.exercise.game.Settings
import cga.exercise.game.Tester
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.random.Random


@OptIn(DelicateCoroutinesApi::class)
fun main(args : Array<String>) {
    val game = Game(1280, 720)
    game.run()
//    runBlocking {
//        var i = 0
//        val f : ((Int)->Unit)= @Synchronized {a-> i+= a}
//
//        val j1 = GlobalScope.launch {
//            repeat(100000){
//                f.invoke(1)
//            }
//        }
//
//        val j2 = GlobalScope.launch {
//            repeat(100000){
//                f.invoke(1)
//            }
//        }
//
//
//        val j3 = GlobalScope.launch {
//            repeat(100000){
//                f.invoke(1)
//            }
//        }
//
//
//        j1.join()
//        j2.join()
//        j3.join()
//        println(i)
//    }
}
