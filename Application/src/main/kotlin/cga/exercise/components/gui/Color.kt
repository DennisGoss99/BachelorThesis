package cga.exercise.components.gui

import org.joml.*

class Color : Vector4f {

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

    fun toVector3f() : Vector3f = Vector3f(x, y, z)

}