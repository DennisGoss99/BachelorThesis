package cga.exercise.game

fun main(args : Array<String>) {

    Tester.saveTester(Tester(cycleSettings= listOf(
        7500L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 0, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        5000L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 10, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        5000L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 50, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        2000L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 100, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        2000L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 500, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        1500L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 1000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        1500L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 5000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        500L to Settings(true, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 10000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),

        7500L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 0, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        5000L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 10, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        5000L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 50, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        2000L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 100, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        2000L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 500, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        1500L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 1000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        1500L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 5000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),
        500L to Settings(false, renderObjects = true, renderVisuals = true, evaluateCollisions = true, applyCollisionEffect = true, applyGravityEffect = true, updateFrequency = 999, objectCount = 10000, useSampleData = true, seed = 1, shatterAmount = 5, impactVelocity = 3f),

        )))
}