package cga.exercise.components.gui

import cga.exercise.game.SceneStats
import cga.exercise.game.StaticResources

class MouseCursor {

    companion object{
        var windowId : Long = 0L

        private var systemCursor = CursorStyle.Arrow
        var systemCursorSet : Boolean = false

        fun setWindowCursor(newCursor: CursorStyle) {
            if(newCursor != systemCursor) {
                val handle : Long? = StaticResources.systemCursors[newCursor]

                if(handle != null && windowId != 0L){
                    systemCursor = newCursor
                    org.lwjgl.glfw.GLFW.glfwSetCursor(windowId, handle)
                }
            }
            systemCursorSet = true
        }

        fun afterUpdate(){
            if(systemCursorSet)
                setWindowCursor(systemCursor)
            else
                setWindowCursor(CursorStyle.Arrow)

            systemCursorSet = false
        }
    }


    enum class CursorStyle {
        Arrow,
        Crosshair,
        Hand,
        HorizontalResize,
        VerticalResize,
        Ibeam
    }
}