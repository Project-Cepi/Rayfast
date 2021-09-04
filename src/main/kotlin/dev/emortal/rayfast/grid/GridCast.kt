package dev.emortal.rayfast.grid

import kotlin.jvm.JvmOverloads
import dev.emortal.rayfast.grid.GridCast.GridIterator
import dev.emortal.rayfast.grid.GridCast.ExactGridIterator
import kotlin.math.abs

object GridCast {
    /**
     * Creates an iterator that iterates through blocks on a 3d grid of the specified grid size, until the total length
     * exceeds the length specified.
     *
     * @param startX iterator start position X
     * @param startY iterator start position Y
     * @param startZ iterator start position Z
     * @param dirX iterator direction X
     * @param dirY iterator direction Y
     * @param dirZ iterator direction Z
     * @param gridSize the size of the grid
     * @param length the maximum length of the iterator
     * @return the iterator
     */
    @JvmStatic
    @JvmOverloads
    fun createGridIterator(
        startX: Double,
        startY: Double,
        startZ: Double,
        dirX: Double,
        dirY: Double,
        dirZ: Double,
        gridSize: Double = 1.0,
        length: Double = Double.MAX_VALUE
    ): Iterator<DoubleArray> {
        return GridIterator(startX, startY, startZ, dirX, dirY, dirZ, gridSize, length)
    }

    /**
     * Creates an iterator that iterates through blocks on a 3d grid of the specified grid size, giving the exact
     * position that was hit when any grid unit was intersected. It does this until the total length exceeds the length
     * specified.
     *
     * @param startX iterator start position X
     * @param startY iterator start position Y
     * @param startZ iterator start position Z
     * @param dirX iterator direction X
     * @param dirY iterator direction Y
     * @param dirZ iterator direction Z
     * @param gridSize the size of the grid
     * @param length the maximum length of the iterator
     * @return the iterator
     */
    fun createExactGridIterator(
        startX: Double,
        startY: Double,
        startZ: Double,
        dirX: Double,
        dirY: Double,
        dirZ: Double,
        gridSize: Double,
        length: Double
    ): Iterator<DoubleArray> {
        return ExactGridIterator(startX, startY, startZ, dirX, dirY, dirZ, gridSize, length)
    }

    private open class GridIterator(
        protected var posX: Double,
        protected var posY: Double,
        protected var posZ: Double,
        protected val dirX: Double,
        protected val dirY: Double,
        protected val dirZ: Double,
        protected val gridSize: Double,
        protected val length: Double
    ) : Iterator<DoubleArray> {
        protected var currentLength = 0.0
        override fun hasNext(): Boolean {
            return currentLength < length
        }

        override fun next(): DoubleArray {
            // Find the length to the next block
            val lengthX = (gridSize - Math.abs(posX) % gridSize) / Math.abs(dirX)
            val lengthY = (gridSize - Math.abs(posY) % gridSize) / Math.abs(dirY)
            val lengthZ = (gridSize - Math.abs(posZ) % gridSize) / Math.abs(dirZ)

            // Find the lowest of all
            val lowest = Math.min(lengthX, Math.min(lengthY, lengthZ))

            // Cast to the next block
            posX += dirX * lowest
            posY += dirY * lowest
            posZ += dirZ * lowest

            // Add length to current
            currentLength += lowest
            return doubleArrayOf(
                posX - posX % gridSize,
                posY - posY % gridSize,
                posZ - posZ % gridSize
            )
        }
    }

    private class ExactGridIterator(
        startX: Double,
        startY: Double,
        startZ: Double,
        dirX: Double,
        dirY: Double,
        dirZ: Double,
        gridSize: Double,
        length: Double
    ) : GridIterator(startX, startY, startZ, dirX, dirY, dirZ, gridSize, length), Iterator<DoubleArray> {
        override fun next(): DoubleArray {
            // Find the length to the next block
            val lengthX = (gridSize - abs(posX) % gridSize) / abs(dirX)
            val lengthY = (gridSize - abs(posY) % gridSize) / abs(dirY)
            val lengthZ = (gridSize - abs(posZ) % gridSize) / abs(dirZ)

            // Find the lowest of all
            val lowest = lengthX.coerceAtMost(lengthY.coerceAtMost(lengthZ))

            // Cast to the next block
            posX += dirX * lowest
            posY += dirY * lowest
            posZ += dirZ * lowest

            // Add length to current
            currentLength += lowest
            return doubleArrayOf(
                posX,
                posY,
                posZ
            )
        }
    }
}