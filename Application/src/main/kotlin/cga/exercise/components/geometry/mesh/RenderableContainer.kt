package cga.exercise.components.geometry.mesh

import cga.exercise.components.geometry.IRenderableContainer
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.shader.ShaderProgram

class RenderableContainer(renderables : HashMap< String ,Renderable>) : HashMap<String, Renderable>(renderables),IRenderableContainer {

    override fun render(shaderProgram: ShaderProgram) {
        super.entries.forEach { e ->

            e.value.render(shaderProgram)
        }
    }

    override fun cleanup() {
        super.entries.forEach { it.value.cleanup()}
    }

}