package dev.emortal.rayfast.area.area3d

import dev.emortal.rayfast.util.Converter

/**
 * Specifies an object that represents some arbitrary 3d area.
 */
interface Area3d {
    /**
     * Returns the intersection between the specified line and this object.
     *
     * @param posX line X position
     * @param posY line Y position
     * @param posZ line Z position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @param dirZ line Z direction
     * @return the computed line intersection position, null if none
     */
    fun lineIntersection(
        posX: Double,
        posY: Double,
        posZ: Double,
        dirX: Double,
        dirY: Double,
        dirZ: Double
    ): DoubleArray?

    /**
     * Returns true if the specified line intersects this object.
     *
     * @param posX line X position
     * @param posY line Y position
     * @param posZ line Z position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @param dirZ line Z direction
     * @return the computed line intersection position, null if none
     */
    fun lineIntersects(posX: Double, posY: Double, posZ: Double, dirX: Double, dirY: Double, dirZ: Double): Boolean {
        return lineIntersection(posX, posY, posZ, dirX, dirY, dirZ) != null
    }

    class Area3dCombined(vararg val all: Area3d) : Area3d {

        override fun lineIntersection(
            posX: Double,
            posY: Double,
            posZ: Double,
            dirX: Double,
            dirY: Double,
            dirZ: Double
        ): DoubleArray? {
            for (area3d in all) {
                val intersection = area3d.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ)
                if (intersection != null) {
                    return intersection
                }
            }
            return null
        }
    }

    companion object {
        /**
         * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
         * dynamism if applicable.
         * <br></br><br></br>
         * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
         *
         * @param area3ds the area3ds to take the planes from
         * @return the new CombinedArea3d or DynamicCombinedArea3d
         */
        @JvmStatic
        fun combined(vararg area3ds: Area3d): Area3dCombined {
            return Area3dCombined(*area3ds)
        }

        /**
         * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
         * dynamism if applicable.
         * <br></br><br></br>
         * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
         *
         * @param area3ds the area3ds to take the planes from
         * @return the new CombinedArea3d or DynamicCombinedArea3d
         */
        @JvmStatic
        fun combined(area3ds: Collection<Area3d>): Area3dCombined {
            return Area3dCombined(*area3ds.toTypedArray())
        }

        @JvmField
        val CONVERTER = Converter<Area3d>()
    }
}