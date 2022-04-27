package cga.exercise.components.gui


import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.SimpleMesh
import cga.exercise.components.gui.TextComponents.TextCursor
import cga.exercise.components.gui.constraints.Center
import cga.exercise.components.gui.constraints.ITranslateConstraint
import cga.exercise.components.gui.constraints.Relative
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.text.FontType
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import java.util.*

class EditText (text : String,
                fontSize : Float,
                font : FontType,
                maxLineLength : Float,
                translateXConstraint : ITranslateConstraint,
                translateYConstraint : ITranslateConstraint,
                color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Text(text, fontSize, font, maxLineLength, translateXConstraint, translateYConstraint, color) {

    init {
        children = listOf(
            TextCursor(Relative(0.0005f * fontSize), Relative(0.009f * fontSize), Center(), Center(), color = Color(220,220,220)))
    }

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)
    }

    override fun render(shaderProgram: ShaderProgram) {
        super.render(shaderProgram)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    override fun cleanup() {
        super.cleanup()
    }

}