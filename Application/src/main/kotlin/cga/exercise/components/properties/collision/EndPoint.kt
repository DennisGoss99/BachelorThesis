package cga.exercise.components.properties.collision

data class EndPoint(
    val owner : IHitBox,
    var value : Float,
    val isMin : Boolean
)