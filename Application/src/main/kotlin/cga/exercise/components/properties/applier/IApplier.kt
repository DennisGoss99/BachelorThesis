package cga.exercise.components.properties.applier

import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.gravity.IGravity

interface IApplier : IGravity, IHitBox {
    var interact : Boolean
}