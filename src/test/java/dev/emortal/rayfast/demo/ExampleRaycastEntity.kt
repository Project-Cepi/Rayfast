package dev.emortal.rayfast.demo

import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism
import dev.emortal.rayfast.util.Point

/**
 * An example usage of an entity with a bounding box.
 *
 * As the entity position changes while the bounding box stays the same, this can be used for multiple raycasts
 * consecutively with no overhead.
 *
 * This is an ideal situation for rayfast.
 */
abstract class ExampleRaycastEntity {
    abstract val boundingBox: BoundingBox?
    abstract val x: Double
    abstract val y: Double
    abstract val z: Double

    inner class BoundingBox(
        private val exampleRaycastEntity: ExampleRaycastEntity,
        width: Double,
        height: Double,
        depth: Double
    ) : Area3dRectangularPrism {
        private val halfWidth: Double = width / 2.0
        private val halfHeight: Double = height / 2.0
        private val halfDepth: Double = depth / 2.0

        // Coordinates
        override val min: Point
            get() = Point(
                exampleRaycastEntity.x - halfWidth,
                exampleRaycastEntity.y - halfHeight,
                exampleRaycastEntity.z - halfDepth
            )
        override val max: Point
            get() = Point(
                exampleRaycastEntity.x + halfWidth,
                exampleRaycastEntity.y + halfHeight,
                exampleRaycastEntity.z + halfDepth
            )

    }
}