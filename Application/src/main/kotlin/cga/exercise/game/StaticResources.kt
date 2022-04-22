package cga.exercise.game

import cga.exercise.components.text.FontType

class StaticResources {
    companion object{

        val fonts = hashMapOf("Arial" to FontType("assets/fonts/Arial.fnt"),
            "Calibri" to FontType("assets/fonts/Calibri.fnt"),
            "Comic Sans MS" to FontType("assets/fonts/Comic Sans MS.fnt"),
            "Times New Roman" to FontType("assets/fonts/Times New Roman.fnt")
        )

        var standardFont = fonts["Arial"]!!

    }

}