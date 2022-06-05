package cga.exercise.components.geometry.hitbox

import cga.exercise.components.properties.collision.HitBox
import cga.exercise.components.shader.ShaderProgram
import kotlinx.coroutines.DelicateCoroutinesApi

interface IHitBoxRenderer {
    var hitboxes: MutableList<HitBox>

    fun add(hitBox: HitBox)
    fun removeHitBoxID(id: Int)
    fun clear()
    fun updateModelMatrix()

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun updateModelMatrixParallel(jobCount: Int)
    fun render(shaderProgram: ShaderProgram)
    fun cleanup()

}