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

}
