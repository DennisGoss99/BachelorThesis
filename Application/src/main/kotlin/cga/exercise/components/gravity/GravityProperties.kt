package cga.exercise.components.gravity

enum class GravityProperties {
    source, // Other IGravity are attracted to it
    adopter, // This IGravity object is moved by other IGravity objects
    sourceAndAdopter // source and adopter combined
}