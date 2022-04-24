package cga.exercise.game

import cga.exercise.components.text.FontType
import org.lwjgl.glfw.GLFW.*

class StaticResources {
    companion object{

        val fonts = hashMapOf("Arial" to FontType("assets/fonts/Arial.fnt"),
            "Calibri" to FontType("assets/fonts/Calibri.fnt"),
            "Comic Sans MS" to FontType("assets/fonts/Comic Sans MS.fnt"),
            "Times New Roman" to FontType("assets/fonts/Times New Roman.fnt")
        )

        var standardFont = fonts["Arial"]!!

        fun keyToCharGERLayout(keyCode : Int, mode : Int) : Char?{
            return when(keyCode){
                GLFW_KEY_SPACE -> ' '
                GLFW_KEY_1 -> if(mode == 1) '!' else '1'
                GLFW_KEY_2 -> if(mode == 1) '"' else '2'
                GLFW_KEY_3 -> if(mode == 1) '§' else '3'
                GLFW_KEY_4 -> if(mode == 1) '$' else '4'
                GLFW_KEY_5 -> if(mode == 1) '%' else '5'
                GLFW_KEY_6 -> if(mode == 1) '&' else '6'
                GLFW_KEY_7 -> if(mode == 1) '/' else '7'
                GLFW_KEY_8 -> if(mode == 1) '(' else '8'
                GLFW_KEY_9 -> if(mode == 1) ')' else '9'
                GLFW_KEY_0 -> if(mode == 1) '=' else '0'
                GLFW_KEY_A -> if(mode == 1) 'A' else 'a'
                GLFW_KEY_B -> if(mode == 1) 'B' else 'b'
                GLFW_KEY_C -> if(mode == 1) 'C' else 'c'
                GLFW_KEY_D -> if(mode == 1) 'D' else 'd'
                GLFW_KEY_E -> if(mode == 1) 'E' else 'e'
                GLFW_KEY_F -> if(mode == 1) 'F' else 'f'
                GLFW_KEY_G -> if(mode == 1) 'G' else 'g'
                GLFW_KEY_H -> if(mode == 1) 'H' else 'h'
                GLFW_KEY_I -> if(mode == 1) 'I' else 'i'
                GLFW_KEY_J -> if(mode == 1) 'J' else 'j'
                GLFW_KEY_K -> if(mode == 1) 'K' else 'k'
                GLFW_KEY_L -> if(mode == 1) 'L' else 'l'
                GLFW_KEY_M -> if(mode == 1) 'M' else 'm'
                GLFW_KEY_N -> if(mode == 1) 'N' else 'n'
                GLFW_KEY_O -> if(mode == 1) 'O' else 'o'
                GLFW_KEY_P -> if(mode == 1) 'P' else 'p'
                GLFW_KEY_Q -> if(mode == 1) 'Q' else 'q'
                GLFW_KEY_R -> if(mode == 1) 'R' else 'r'
                GLFW_KEY_S -> if(mode == 1) 'S' else 's'
                GLFW_KEY_T -> if(mode == 1) 'T' else 't'
                GLFW_KEY_U -> if(mode == 1) 'U' else 'u'
                GLFW_KEY_V -> if(mode == 1) 'V' else 'v'
                GLFW_KEY_W -> if(mode == 1) 'W' else 'w'
                GLFW_KEY_X -> if(mode == 1) 'X' else 'x'
                GLFW_KEY_Z -> if(mode == 1) 'Y' else 'y'
                GLFW_KEY_Y -> if(mode == 1) 'Z' else 'z'
                GLFW_KEY_COMMA -> if(mode == 1) ';' else ','
                91 -> if(mode == 1) 'Ü' else 'ü'
                92 -> if(mode == 1) '\'' else '#'
                93 -> if(mode == 1) '*' else '+'
                59 -> if(mode == 1) 'Ö' else 'ö'
                39 -> if(mode == 1) 'Ä' else 'ä'
                162 -> if(mode == 1) '>' else '<'
                46 -> if(mode == 1) ':' else '.'
                47 -> if(mode == 1) '_' else '-'
                45 -> if(mode == 1) '?' else 'ß'

                //Numpad
                GLFW_KEY_KP_0 -> '0'
                GLFW_KEY_KP_1 -> '1'
                GLFW_KEY_KP_2-> '2'
                GLFW_KEY_KP_3-> '3'
                GLFW_KEY_KP_4 -> '4'
                GLFW_KEY_KP_5 -> '5'
                GLFW_KEY_KP_6 -> '6'
                GLFW_KEY_KP_7 -> '7'
                GLFW_KEY_KP_8 -> '8'
                GLFW_KEY_KP_9 -> '9'
                GLFW_KEY_KP_ADD -> '+'
                GLFW_KEY_KP_MULTIPLY -> '*'
                GLFW_KEY_KP_DIVIDE -> '/'
                GLFW_KEY_KP_SUBTRACT -> '-'
                GLFW_KEY_KP_DECIMAL -> ','

                else -> null
            }

        }

    }

}