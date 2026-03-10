@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.LevelSimulatedReader
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

val ROOT_SHOULD_SKIP: (BlockState) -> Boolean = { state ->
    state.`is`(CarminiteBlockTags.ROOT_TRACE_SKIP)
}
val IS_REPLACEABLE_AIR: (BlockState) -> Boolean = { state ->
    state.canBeReplaced() || state.isAir
}

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

fun isReplaceable(
    state: BlockState,
    includeFlowers: Boolean
): Boolean {
    return (state.canBeReplaced() ||
            state.`is`(CarminiteBlockTags.WORLDGEN_REPLACEABLES) ||
            (includeFlowers && state.`is`(BlockTags.FLOWERS))) &&
            !state.`is`(BlockTags.FEATURES_CANNOT_REPLACE)
}

fun worldGenReplaceable(
    state: BlockState
): Boolean {
    return isReplaceable(state, includeFlowers = false)
}

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