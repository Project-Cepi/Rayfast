package dev.emortal.rayfast.util

import org.jetbrains.annotations.ApiStatus

/**
 * Internal Intersection Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@ApiStatus.Internal
object IntersectionUtils {
    fun forwardPlaneIntersection( // Line
        pos: Point,  // Position vector
        dir: Point,  // Direction vector
        // Plane
        min: Point,
        adj: Point,
        max: Point
    ): Point? {
        val posIntersection = planeIntersection(
            pos,
            dir,
            min,
            adj,
            max
        ) ?: return null

        // Check if position is forwards
        val dotProduct = dir.x * (posIntersection.x - pos.x) + dir.y * (posIntersection.y - pos.y) + dir.z * (posIntersection.z - pos.z)
        return if (dotProduct > 0) {
            pos
        } else null
    }

    fun planeIntersection( // Line
        pos: Point,  // Position vector
        dir: Point,  // Direction vector
        // Plane
        min: Point,
        adj: Point,
        max: Point
    ): Point? {
        val point = getIntersection(
            pos,
            dir,
            min,
            adj,
            max
        )

        val (x, y, z) = point
        var fits = 0

        if (min.x != max.x && x in min.x..max.x) {
            fits++
        }
        if (min.y != max.y && y in min.y..max.y) {
            fits++
        }
        if (min.z != max.z && z in min.z..max.z) {
            fits++
        }
        return if (fits < 2) {
            null
        } else Point(x, y, z)
    }

    /**
     * Gets the intersections of the specified line with the specified planes
     *
     * @param dirX   Line Direction X
     * @param dirY   Line Direction Y
     * @param dirZ   Line Direction Z
     * @param posX   Line Position X
     * @param posY   Line Position Y
     * @param posZ   Line Position Z
     * @param planes   Array of planes. Each plane contains 3 X, Y, and Z coordinates, with 9 elements in total. Example:
     * @`
     * {
     * {
     * 1.3, -5.9, 3.0,
     * 1.1, -5.3, 3.4,
     * 0.3, -3.9, 3.2,
     * }
     * }
    ` *
     * @return Array of intersection positions.
     */
    fun intersectPlanes( // Line
        pos: Point,  // Position vector
        dir: Point,  // Direction vector
        // Planes
        vararg planes: Array<Point>
    ) = Array(planes.size) {
        val plane = planes[it]

        getIntersection( // Line
            pos,
            dir,  // Plane
            plane[0],
            plane[1],
            plane[2]
        )
    }


    fun getIntersection( // Line
        pos: Point,  // Position vector
        dir: Point,  // Direction vector
        // Plane
        plane: Point,  // Plane point
        planeDir: Point // Plane normal
    ): Point {
        // Sensitive (speed oriented) code:
        val dotA = planeDir.x * plane.x + planeDir.y * plane.y + planeDir.z * plane.z
        val dotB = planeDir.x * pos.x + planeDir.y * pos.y + planeDir.z * pos.z
        val dotC = planeDir.x * dir.x + planeDir.y * dir.y + planeDir.z * dir.z
        val t = (dotA - dotB) / dotC
        val x = pos.x + dir.x * t
        val y = pos.y + dir.y * t
        val z = pos.z + dir.z * t
        return Point(x, y, z)
    }

    private fun getDot(
        x: Double, y: Double, z: Double,
        vecX: Double, vecY: Double, vecZ: Double
    ): Double {
        return x * vecX + y * vecY + z * vecZ
    }

    fun getIntersection( // Line
        pos: Point,  // Position vector
        dir: Point,  // Direction vector
        // Plane
        min: Point,
        adj: Point,
        max: Point
    ): Point {
        val v1x = min.x - adj.x
        val v1y = min.y - adj.y
        val v1z = min.z - adj.z
        val v2x = min.x - max.x
        val v2y = min.y - max.y
        val v2z = min.z - max.z
        val crossX = v1y * v2z - v2y * v1z
        val crossY = v1z * v2x - v2z * v1x
        val crossZ = v1x * v2y - v2x * v1y
        return getIntersection( // Line
            pos,
            dir,  // Plane
            min,
            Point(crossX, crossY, crossZ)
        )

        // TODO: fix this (faster) method
        /*
        System.out.println("LINE:");
        System.out.println("DIR: " + dirX + ":" + dirY + ":" + dirZ);
        System.out.println("POS: " + posX + ":" + posY + ":" + posZ);

        double ABx = maxX - minX;
        double ABy = maxY - minY;
        double ABz = maxZ - minZ;

        double ACx = adjX - minX;
        double ACy = adjY - minY;
        double ACz = adjZ - minZ;

        double perpenX = ABy * ACz - ACy * ABz;
        double perpenY = ACx * ABz - ABx * ACz;
        double perpenZ = ABx * ACy - ACx * ABy;

        // Line equation: r = pos + t * dir
        // x = posX + (dirX * t)
        // y = posY + (dirY * t)
        // z = posZ + (dirZ * t)

        // Plane equation
        // 0 = pointX(x - perpenX) + pointY(y - perpenY) + pointZ(z - perpenZ)
        // 0 = pointX(posX + (dirX * t) - perpenX) + pointY(posY + (dirY * t) - perpenY) + pointZ(posZ + (dirZ * t) - perpenZ)

        // Combine equations to find t (distance to point)
        // 0 = g(d + (a * t) - j) + h(e + (b * t) - k) + i(f + (c * t) - l)
        // t = (-dg+jg-if-eh+li+kh) / (ag+bh+ci)
        double t = (-posX * adjX + perpenX * adjX - adjZ * posZ - posY * adjY + perpenZ * adjZ + perpenY * adjY) / (dirX * adjX + dirY * adjY + dirZ * adjZ);

        // A = -B * C

        // Now use t to get the point
        // x = posX + (dirX * t)
        // y = posY + (dirY * t)
        // z = posZ + (dirZ * t)

        double x = posX + (dirX * t);
        double y = posY + (dirY * t);
        double z = posZ + (dirZ * t);
        return new double[] {x, y, z};
        */
    }
}