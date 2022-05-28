package cga.exercise.game

import cga.exercise.components.gui.Color
import cga.exercise.components.gui.MouseCursor.*
import cga.exercise.components.text.FontType
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*

class StaticResources {
    companion object{

        val fonts = listOf(
            FontType("assets/fonts/Arial.fnt"),
            FontType("assets/fonts/Calibri.fnt"),
            FontType("assets/fonts/Comic Sans MS.fnt"),
            FontType("assets/fonts/Times New Roman.fnt"),
            FontType("assets/fonts/Proxima Nova Light.fnt")
        )

        val standardFont = fonts[4]
        val arial = fonts[0]
        val calibri = fonts[1]
        val comicSans = fonts[2]
        val timesNewRoman = fonts[3]
        val ProximaNova = fonts[4]

        fun keyToCharGERLayout(keyCode : Int, mode : Int) : Char?{
            return when(keyCode){
                GLFW_KEY_SPACE -> ' '
                GLFW_KEY_ENTER -> '\n'
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

        fun keyIsDigit(keyCode : Int, mode : Int) : Boolean{
            return when(keyCode) {
                GLFW_KEY_1,
                GLFW_KEY_2,
                GLFW_KEY_3,
                GLFW_KEY_4,
                GLFW_KEY_5,
                GLFW_KEY_6,
                GLFW_KEY_7,
                GLFW_KEY_8,
                GLFW_KEY_9,
                GLFW_KEY_0 -> mode == 0

                //Numpad
                GLFW_KEY_KP_0,
                GLFW_KEY_KP_1,
                GLFW_KEY_KP_2,
                GLFW_KEY_KP_3,
                GLFW_KEY_KP_4,
                GLFW_KEY_KP_5,
                GLFW_KEY_KP_6,
                GLFW_KEY_KP_7,
                GLFW_KEY_KP_8,
                GLFW_KEY_KP_9 -> true

                else -> false
            }
        }

        fun keyIsInstruction(keyCode : Int, mode : Int) : Boolean{
            return when(keyCode){
                GLFW_KEY_DELETE,
                GLFW_KEY_END,
                GLFW_KEY_HOME,
                GLFW_KEY_RIGHT,
                GLFW_KEY_LEFT,
                GLFW_KEY_DOWN,
                GLFW_KEY_UP,
                GLFW_KEY_SPACE,
                GLFW_KEY_BACKSPACE,
                GLFW_KEY_ENTER -> true
                else -> false
            }
        }

        val backGroundColor = Color(18,18,18)
        val highlightColor = Color.blue
        val disableColor = Color.silver
        val activeColor = Color(29, 184, 83)
        val activeColor2 = Color(80, 80, 80)
        val componentColor = Color(51, 51, 51)
        val componentColor2 = Color(34, 34, 34)
        val componentColor3 = Color(80, 80, 80)
        val componentColor4 = Color(246, 246, 246)
        val fontColor = Color(255, 255, 255)
        val fontColor1 = Color(156, 156, 156)
        val fontColorDisabled = Color(200, 200, 200)

        val systemCursors = mapOf<CursorStyle, Long?>(
            CursorStyle.Arrow to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR),
            CursorStyle.Crosshair to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_CROSSHAIR_CURSOR),
            CursorStyle.Hand to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR),
            CursorStyle.HorizontalResize to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR),
            CursorStyle.VerticalResize to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR),
            CursorStyle.Ibeam to org.lwjgl.glfw.GLFW.glfwCreateStandardCursor(org.lwjgl.glfw.GLFW.GLFW_IBEAM_CURSOR)
        )
    }

}