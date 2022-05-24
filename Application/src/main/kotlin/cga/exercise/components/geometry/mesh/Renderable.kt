package cga.exercise.components.geometry.mesh

import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

open class Renderable(renderable : RenderableBase) : RenderableBase(renderable.meshes, renderable.modelMatrix, renderable.parent)