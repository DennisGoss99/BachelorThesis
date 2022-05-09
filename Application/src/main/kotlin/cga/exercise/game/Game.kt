package cga.exercise.game

import cga.exercise.components.gui.MouseCursor
import cga.framework.GameWindow

/*
  Created by Fabian on 16.09.2017.
 */
class Game(width: Int,
           height: Int,
           fullscreen: Boolean = false,
           vsync: Boolean = false,
           title: String = "OuterSpace",
           GLVersionMajor: Int = 3,
           GLVersionMinor: Int = 3) : GameWindow(width, height, fullscreen, vsync, GLVersionMajor, GLVersionMinor, title, 4, 60f) {

    private val scene: Scene
    init {
        setCursorVisible(true)
        scene = Scene(this)
    }

    override fun shutdown() = scene.cleanup()

    override suspend fun update(dt: Float, t: Float) {
        scene.update(dt, t)
        MouseCursor.afterUpdate()
    }

    override fun render(dt: Float, t: Float) = scene.render(dt, t)

    override fun onMouseMove(xpos: Double, ypos: Double) = scene.onMouseMove(xpos, ypos)

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int) = scene.onKey(key, scancode, action, mode)

    override fun onMouseScroll(xoffset: Double, yoffset: Double) = scene.onMouseScroll(xoffset, yoffset)

    override fun onMouseButton(button: Int, action: Int, mode: Int) = scene.onMouseButton(button, action, mode)

    override fun onWindowSize(width: Int, height: Int) {
        super.onWindowSize(width, height)
        scene.onWindowSize(width, height)
    }
}