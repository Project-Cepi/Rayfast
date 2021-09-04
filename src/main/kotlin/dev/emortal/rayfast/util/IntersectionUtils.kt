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
        posX: Double, posY: Double, posZ: Double,  // Position vector
        dirX: Double, dirY: Double, dirZ: Double,  // Direction vector
        // Plane
        minX: Double, minY: Double, minZ: Double,
        adjX: Double, adjY: Double, adjZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): DoubleArray? {
        val pos = planeIntersection(
            posX, posY, posZ,
            dirX, dirY, dirZ,
            minX, minY, minZ,
            adjX, adjY, adjZ,
            maxX, maxY, maxZ
        ) ?: return null

        // Check if position is forwards
        val dotProduct = dirX * (pos[0] - posX) + dirY * (pos[1] - posY) + dirZ * (pos[2] - posZ)
        return if (dotProduct > 0) {
            pos
        } else null
    }

    fun planeIntersection( // Line
        posX: Double, posY: Double, posZ: Double,  // Position vector
        dirX: Double, dirY: Double, dirZ: Double,  // Direction vector
        // Plane
        minX: Double, minY: Double, minZ: Double,
        adjX: Double, adjY: Double, adjZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): DoubleArray? {
        val point = getIntersection(
            posX, posY, posZ,
            dirX, dirY, dirZ,
            minX, minY, minZ,
            adjX, adjY, adjZ,
            maxX, maxY, maxZ
        )

        val (x, y, z) = point
        var fits = 0

        if (minX != maxX && x in minX..maxX) {
            fits++
        }
        if (minY != maxY && y in minY..maxY) {
            fits++
        }
        if (minZ != maxZ && z in minZ..maxZ) {
            fits++
        }
        return if (fits < 2) {
            null
        } else doubleArrayOf(x, y, z)
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
        posX: Double, posY: Double, posZ: Double,  // Position vector
        dirX: Double, dirY: Double, dirZ: Double,  // Direction vector
        // Planes
        vararg planes: DoubleArray
    ) = Array(planes.size) {
        val plane = planes[it]

        getIntersection( // Line
            posX, posY, posZ,
            dirX, dirY, dirZ,  // Plane
            plane[0], plane[1], plane[2],
            plane[3], plane[4], plane[5],
            plane[6], plane[7], plane[8]
        )
    }


    fun getIntersection( // Line
        posX: Double, posY: Double, posZ: Double,  // Position vector
        dirX: Double, dirY: Double, dirZ: Double,  // Direction vector
        // Plane
        planeX: Double, planeY: Double, planeZ: Double,  // Plane point
        planeDirX: Double, planeDirY: Double, planeDirZ: Double // Plane normal
    ): Point {
        // Sensitive (speed oriented) code:
        val dotA = planeDirX * planeX + planeDirY * planeY + planeDirZ * planeZ
        val dotB = planeDirX * posX + planeDirY * posY + planeDirZ * posZ
        val dotC = planeDirX * dirX + planeDirY * dirY + planeDirZ * dirZ
        val t = (dotA - dotB) / dotC
        val x = posX + dirX * t
        val y = posY + dirY * t
        val z = posZ + dirZ * t
        return Point(x, y, z)
    }

    private fun getDot(
        x: Double, y: Double, z: Double,
        vecX: Double, vecY: Double, vecZ: Double
    ): Double {
        return x * vecX + y * vecY + z * vecZ
    }

    fun getIntersection( // Line
        posX: Double, posY: Double, posZ: Double,  // Position vector
        dirX: Double, dirY: Double, dirZ: Double,  // Direction vector
        // Plane
        minX: Double, minY: Double, minZ: Double,
        adjX: Double, adjY: Double, adjZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): Point {
        val v1x = minX - adjX
        val v1y = minY - adjY
        val v1z = minZ - adjZ
        val v2x = minX - maxX
        val v2y = minY - maxY
        val v2z = minZ - maxZ
        val crossX = v1y * v2z - v2y * v1z
        val crossY = v1z * v2x - v2z * v1x
        val crossZ = v1x * v2y - v2x * v1y
        return getIntersection( // Line
            posX, posY, posZ,
            dirX, dirY, dirZ,  // Plane
            minX, minY, minZ,
            crossX, crossY, crossZ
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