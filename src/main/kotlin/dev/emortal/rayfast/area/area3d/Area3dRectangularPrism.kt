package dev.emortal.rayfast.area.area3d

import dev.emortal.rayfast.util.IntersectionUtils
import java.util.function.Function

/**
 * A static rectangular prism class
 */
interface Area3dRectangularPrism : Area3d {
    // Coordinates
    val minX: Double
    val minY: Double
    val minZ: Double
    val maxX: Double
    val maxY: Double
    val maxZ: Double
    override fun lineIntersection(
        posX: Double,
        posY: Double,
        posZ: Double,
        dirX: Double,
        dirY: Double,
        dirZ: Double
    ): DoubleArray? {
        run {
            // Front
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Back
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                minX, minY, maxZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Left
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                minX, minY, minZ,
                minX, maxY, minZ,
                minX, maxY, maxZ
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Right
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ
            )
            if (intersection != null) {
                return intersection
            }
        }
        run {
            // Top
            val intersection = IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                minX, maxY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ
            )
            if (intersection != null) {
                return intersection
            }
        }
        run { // Bottom
            return IntersectionUtils.forwardPlaneIntersection( // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,  // Plane
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ
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
         * @param minXGetter the getter for minX
         * @param minYGetter the getter for minY
         * @param minZGetter the getter for minZ
         * @param maxXGetter the getter for maxX
         * @param maxYGetter the getter for maxY
         * @param maxZGetter the getter for maxZ
         * @param <T> the type of the wrapper
         * @return the area that is represented by this wrapped
        </T> */
        @JvmStatic
        fun <T> wrapper(
            `object`: T,
            minXGetter: Function<T, Double>,
            minYGetter: Function<T, Double>,
            minZGetter: Function<T, Double>,
            maxXGetter: Function<T, Double>,
            maxYGetter: Function<T, Double>,
            maxZGetter: Function<T, Double>
        ): Area3dRectangularPrism {
            return object : Area3dRectangularPrism {
                override val minX: Double
                    get() = minXGetter.apply(`object`)
                override val minY: Double
                    get() = minYGetter.apply(`object`)
                override val minZ: Double
                    get() = minZGetter.apply(`object`)
                override val maxX: Double
                    get() = maxXGetter.apply(`object`)
                override val maxY: Double
                    get() = maxYGetter.apply(`object`)
                override val maxZ: Double
                    get() = maxZGetter.apply(`object`)
            }
        }
    }
}