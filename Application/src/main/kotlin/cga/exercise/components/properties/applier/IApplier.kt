package cga.exercise.components.properties.applier

import cga.exercise.components.properties.collision.IHitBox
import cga.exercise.components.properties.gravity.IGravity
import java.util.concurrent.atomic.AtomicBoolean

interface IApplier : IGravity, IHitBox {
    var interact : Boolean
    var checked : AtomicBoolean
}