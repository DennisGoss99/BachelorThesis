package cga.exercise.components.gui

import org.joml.*

class Color : Vector4f {

    companion object{

        val turquoise = Color(26, 188, 156)
        val emerald = Color(46, 204, 113)
        val lightBlue = Color(52, 152, 219)
        val amethyst = Color(155, 89, 182)
        val lightMidnight = Color(52, 73, 94)
        val green = Color(39, 174, 96)
        val blue = Color(9, 132, 217)
        val purple = Color(142, 68, 173)
        val midnight = Color(44, 62, 80)
        val yellow = Color(241, 196, 15)
        val carrot = Color(230, 126, 34)
        val red = Color(231, 76, 60)
        val withe2 = Color(236, 240, 241)
        val lightGrey = Color(210, 214, 209)
        val orange = Color(243, 156, 18)
        val pumpkin = Color(211, 84, 0)
        val darkRed = Color(192, 57, 43)
        val silver = Color(189, 195, 199)
        val grey = Color(97, 101, 106)
        val darkGrey = Color(45, 52, 54)

        val black = Color(0,0,0)
        val withe = Color(255,255,255)
    }

    constructor(hex : String){
        var curser = 0

        if(hex.startsWith("#"))
            curser = 1
        if(hex.startsWith("0x"))
            curser = 2

        x = Integer.parseInt(hex.substring(curser,curser + 2),16) / 255f
        curser += 2
        y = Integer.parseInt(hex.substring(curser,curser + 2),16) / 255f
        curser += 2
        z = Integer.parseInt(hex.substring(curser,curser + 2),16) / 255f
        curser += 2
        if(curser >= hex.length){
            w = 1f
            return
        }
        w = Integer.parseInt(hex.substring(curser,curser + 2),16) / 255f
    }

    constructor(r : Int, g : Int, b : Int, a : Int = 255){
        x = r / 255f
        y = g / 255f
        z = b / 255f
        w = a / 255f
    }

    constructor(r : Float, g : Float, b : Float, a : Float = 255f){
        x = r / 255f
        y = g / 255f
        z = b / 255f
        w = a / 255f
    }

    fun getCopy(adder : Float) : Vector4f{
        return Vector4f(x + adder / 255f,y + adder / 255f,z + adder / 255f, w)
    }

    fun toVector3f() : Vector3f = Vector3f(x, y, z)

}