package dev.emortal.rayfast.area.area3d

import dev.emortal.rayfast.util.Converter
import dev.emortal.rayfast.util.Point

/**
 * Specifies an object that represents some arbitrary 3d area.
 */
interface Area3d {
    /**
     * Returns the intersection between the specified line and this object.
     *
     * @param pos line position
     * @param dir line direction
     * @return the computed line intersection position, null if none
     */
    fun lineIntersection(
        pos: Point,
        dir: Point
    ): Point?

    /**
     * Returns true if the specified line intersects this object.
     *
     * @param pos line position
     * @param dir line direction
     * @return the computed line intersection position, null if none
     */
    fun lineIntersects(pos: Point, dir: Point): Boolean {
        return lineIntersection(pos, dir) != null
    }

    class Area3dCombined(vararg val all: Area3d) : Area3d {

        override fun lineIntersection(
            pos: Point,
            dir: Point
        ): Point? {
            for (area3d in all) {
                val intersection = area3d.lineIntersection(pos, dir)
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