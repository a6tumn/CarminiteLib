@file:Suppress("unused")

package io.autumn.carminite.api.featureutil.v1

import net.minecraft.core.BlockPos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Collection of helper methods for streamlining feature creation.
 *
 * Default parameter values are not provided in most cases as the
 * arguments will need meaningful values for production code.
 **/

/**
 * Translates a given [BlockPos] in 3D space using spherical coordinates.
 *
 * Intended to be used on a given [BlockPos].
 *
 * @param pos The starting [BlockPos] to translate (no default set).
 * @param distance The distance to move from the starting position (no default set).
 * @param angle The horizontal rotation around the Y-axis, in fractions of a full circle (0.0..1.0 corresponds to 0°..360°) (no default set).
 * @param tilt The vertical tilt angle, in fractions of π radians (0.0 is horizontal, 1.0 is 180°) (no default set).
 **/
fun translate(
    pos: BlockPos,
    distance: Double,
    angle: Double,
    tilt: Double
): BlockPos {
    val rAngle = angle * 2.0 * Math.PI
    val rTilt = tilt * Math.PI

    val dx = (sin(rAngle) * sin(rTilt) * distance).roundToInt()
    val dy = (cos(rTilt) * distance).roundToInt()
    val dz = (cos(rAngle) * sin(rTilt) * distance).roundToInt()

    return pos.offset(dx, dy, dz)
}