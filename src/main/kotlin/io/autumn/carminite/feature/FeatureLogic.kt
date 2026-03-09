@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Collection of helper methods for streamlining feature creation.
 *
 * Default parameter values are not provided in most cases as the
 * arguments will need meaningful values for production code.
 **/

val ROOT_SHOULD_SKIP: (BlockState) -> Boolean = { state ->
    state.`is`(CarminiteBlockTags.ROOT_TRACE_SKIP)
}
val IS_REPLACEABLE_AIR: (BlockState) -> Boolean = { state ->
    state.canBeReplaced() || state.isAir
}

/**
 * Checks if a given [BlockPos] in a world has at least one empty neighboring block,
 * ignoring the block directly below.
 *
 * Intended to be used to determine if roots should grow exposed.
 *
 * @param worldReader The world reader used to query block states (no default set).
 * @param pos The position to check neighbors of (no default set).
 **/
fun hasEmptyNeighborExceptBelow(
    worldReader: LevelSimulatedReader,
    pos: BlockPos
): Boolean {
    return worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR) ||
            worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR) ||
            worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR) ||
            worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR) ||
            worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR)
}

/**
 * Checks if a given [BlockPos] in a world has any solid neighbors.
 *
 * Intended to be used to determine if roots can grow in a block.
 *
 * @param worldReader The world reader used to query block states (no default set).
 * @param pos The position to check neighbors of (no default set).
 **/
fun hasSolidNeighbor(
    worldReader: LevelSimulatedReader,
    pos: BlockPos
): Boolean {
    return !(worldReader.isStateAtPosition(pos.below(), IS_REPLACEABLE_AIR) &&
            worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR) &&
            worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR) &&
            worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR) &&
            worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR) &&
            worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR))
}

/**
 * Checks if a given [BlockState] is considered replaceable by roots.
 *
 * Intended to be used when tracing root placement in the world.
 *
 * @param state The block state to check (no default set).
 * @param includeFlowers Whether blocks tagged as flowers should be considered replaceable (no default set).
 **/
fun isReplaceable(
    state: BlockState,
    includeFlowers: Boolean
): Boolean {
    return (state.canBeReplaced() ||
            state.`is`(CarminiteBlockTags.WORLDGEN_REPLACEABLES) ||
            (includeFlowers && state.`is`(BlockTags.FLOWERS))) &&
            !state.`is`(BlockTags.FEATURES_CANNOT_REPLACE)
}

/**
 * Checks if a given [BlockState] can be replaced during world generation.
 *
 * Intended to be used when placing roots or features in the world.
 *
 * @param state The block state to check (no default set).
 **/
fun worldGenReplaceable(
    state: BlockState
): Boolean {
    return isReplaceable(state, includeFlowers = false)
}

/**
 * Determines if a root can grow into a given [BlockPos] based on surrounding blocks.
 *
 * Intended to be used when tracing underground root placement.
 *
 * @param worldReader The world reader used to query block states (no default set).
 * @param pos The position to check for root growth (no default set).
 **/
fun canRootGrowIn(
    worldReader: LevelSimulatedReader,
    pos: BlockPos
): Boolean {
    return if (worldReader.isStateAtPosition(pos, IS_REPLACEABLE_AIR)) {
        hasSolidNeighbor(worldReader, pos)
    } else {
        worldReader.isStateAtPosition(pos) { state -> worldGenReplaceable(state) }
    }
}

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