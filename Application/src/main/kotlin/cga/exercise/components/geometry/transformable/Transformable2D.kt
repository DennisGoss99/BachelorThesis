package cga.exercise.components.geometry.transformable

import cga.framework.WindowStats
import org.joml.*

open class Transformable2D (var modelMatrix : Matrix4f = Matrix4f(), var parent: Transformable2D? = null) {

    //fun rotateLocal( roll: Float) = modelMatrix.rotateXYZ(0f,0f,toRadians(roll));

    fun translateLocal(deltaPos: Vector2f){
        modelMatrix.translate(Vector3f(deltaPos,0f))
    }

    fun translateGlobal(deltaPos: Vector2f){
        val tempMatrix = Matrix4f().translate(Vector3f(deltaPos,0f))
        modelMatrix = tempMatrix.mul(modelMatrix);
    }

    fun scaleLocal(scale: Vector2f) = modelMatrix.scale(Vector3f(scale,1f))

    fun getWorldModelMatrix(): Matrix4f {
        // transformable parent abfragen, wenn ja links multiplikation der partent matrix wenn nein dann selbst world matrix, kein Patent kopie von Modelmatrix
        val world = Matrix4f(modelMatrix)
        if (parent != null) {
            parent!!.getWorldModelMatrix().mul(modelMatrix, world)
        }
        return world
    }

    fun getLocalModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }

    fun getPosition(): Vector2f {
        val tempVector = Vector3f();
        modelMatrix.getColumn(3,tempVector)
        return Vector2f(tempVector.x,tempVector.y);
    }

    fun getWorldPosition(): Vector2f {
        val tempVector = Vector3f();
        getWorldModelMatrix().getColumn(3, tempVector)
        return Vector2f(tempVector.x,tempVector.y);
    }

    fun getScaleLocal(): Vector2f {
        return Vector2f(modelMatrix.m00(),modelMatrix.m11())
    }

    fun getWorldScale(): Vector2f {
        val worldModelMatrix = getWorldModelMatrix()
        return Vector2f(worldModelMatrix.m00(),worldModelMatrix.m11())
    }

    fun setPosition(position: Vector2f){
        modelMatrix.set(3,0, position.x)
        modelMatrix.set(3,1, position.y)
    }

    fun getWorldPixelPosition() : Vector4f{

        val scale = getWorldScale()
        val translate = getWorldPosition()

        // ScalePos Lower Right
        val scaleX2 = WindowStats.windowWidth * (1 - scale.x) / 2f
        val scaleY2= WindowStats.windowHeight * (1 - scale.y) / 2f

        // ScalePos Upper Left
        val scaleX1 = WindowStats.windowWidth - scaleX2
        val scaleY1 = WindowStats.windowHeight - scaleY2

        val transX = WindowStats.windowWidth / 2f * translate.x
        val transY = WindowStats.windowHeight / 2f * translate.y

        // contains x1,y1,x2,y2
        return Vector4f(scaleX1 - transX, scaleY1 + transY, scaleX2 - transX, scaleY2 + transY)
    }


}