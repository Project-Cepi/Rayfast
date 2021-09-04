package dev.emortal.rayfast.area.area3d

import dev.emortal.rayfast.util.IntersectionUtils
import dev.emortal.rayfast.util.Point

/**
 * A static rectangular prism class
 */
interface Area3dRectangularPrism : Area3d {
    // Coordinates
    val min: Point
    val max: Point
    override fun lineIntersection(
        pos: Point,
        dir: Point
    ): Point? {
        run {
            // Front
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(min.x, min.y, min.z),
                Point(min.x, max.y, min.z),
                Point(max.x, max.y, min.z)
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Back
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(min.x, min.y, max.z),
                Point(min.x, max.y, max.z),
                Point(max.x, max.y, max.z)
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Left
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(min.x, min.y, min.z),
                Point(min.x, max.y, min.z),
                Point(min.x, max.y, max.z)
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Right
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(max.x, min.y, min.z),
                Point(max.x, max.y, min.z),
                Point(max.x, max.y, max.z)
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Top
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(min.x, max.y, min.z),
                Point(max.x, max.y, min.z),
                Point(max.x, max.y, max.z)
            )
            if (intersection != null) {
                return intersection
            }
        }
        run { // Bottom
            return IntersectionUtils.forwardPlaneIntersection( // Line
                pos,
                dir,  // Plane
                Point(min.x, min.y, min.z),
                Point(max.x, min.y, min.z),
                Point(max.x, min.y, max.z)
            )
        }
    }

    companion object {
        /**
         * Generates a wrapper for the specified object using the specified getters.
         * <br></br><br></br>
         * This is a sub-optimal implementation due to java generic limitations. An ideal implementation implements the
         * Area3dRectangularPrism interface directly on the object.
         *
         * @param object the object to wrap
         * @param minGetter the getter for min
         * @param maxGetter the getter for max
         * @param <T> the type of the wrapper
         * @return the area that is represented by this wrapped
        </T> */
        @JvmStatic
        inline fun <T> wrapper(
            `object`: T,
            crossinline minGetter: (T) -> Point,
            crossinline maxGetter: (T) -> Point,
        ) = object : Area3dRectangularPrism {
            override val min: Point
                get() = minGetter(`object`)
            override val max: Point
                get() = maxGetter(`object`)
        }
    }
}