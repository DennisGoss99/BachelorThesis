package cga.exercise.components.properties.gravity

enum class GravityProperties {
    source, // Other IGravity are attracted to it
    adopter, // This IGravity object is moved by other IGravity objects
    sourceAndAdopter, // source and adopter combined
    nothing // Doesn't interact with other objects (only the own velocity will be applied)
}