@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.BlockPos

/**
 * Collection of utility methods for streamlining feature creation.
 *
 * Default parameter values are not provided in most cases as the
 * arguments will need meaningful values for production code.
 **/

/**
 * Checks if any block below a given [BlockPos] within a specified depth
 * satisfies a given [predicate].
 *
 * Intended to be used when tracing root growth or other vertical block checks.
 *
 * @param exposedPos The starting position from which to check downward (no default set).
 * @param depth The number of blocks to check below the starting position (no default set).
 * @param predicate A function that returns `true` if a block matches the condition (no default set).
 **/
fun anyBelowMatch(exposedPos: BlockPos, depth: Int, predicate: (BlockPos) -> Boolean): Boolean {
    return isAnyMatchInArea(exposedPos.below(depth), 1, depth + 1, 1, predicate)
}

/**
 * Checks if any block within a rectangular area satisfies a given [predicate].
 *
 * Intended to be used for generalized 3D block checks around a position.
 *
 * @param pos The starting position of the area to check (no default set).
 * @param xWidth The number of blocks to check along the X-axis (no default set).
 * @param height The number of blocks to check along the Y-axis (no default set).
 * @param zWidth The number of blocks to check along the Z-axis (no default set).
 * @param predicate A function that returns `true` if a block matches the condition (no default set).
 **/
fun isAnyMatchInArea(pos: BlockPos, xWidth: Int, height: Int, zWidth: Int, predicate: (BlockPos) -> Boolean): Boolean {
    for (cx in 0 until xWidth) {
        for (cy in 0 until height) {
            for (cz in 0 until zWidth) {
                val checkPos = pos.offset(cx, cy, cz)
                if (predicate(checkPos)) return true
            }
        }
    }
    return false
}