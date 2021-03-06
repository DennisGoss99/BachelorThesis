package cga.exercise.components.geometry.atmosphere

import cga.exercise.components.gui.Color
import cga.exercise.components.geometry.material.SimpleMaterial
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D

class AtmosphereMaterial(texture: Texture2D, var color : Color) : SimpleMaterial(texture) {

    override fun bind(shaderProgram: ShaderProgram) {
        super.bind(shaderProgram)

        shaderProgram.setUniform("textureColor", color)
    }

}