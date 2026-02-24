@file:Suppress("unused")

package io.autumn.carminite.api.featureutil.v1
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import java.util.function.BiConsumer

/**
 * Collection of helper methods for streamlining feature placer creation.
 *
 * Default parameter values are not provided in most cases as the
 * arguments will need meaningful values for production code.
 **/

/**
 * Predicate to check if a given [BlockPos] in a world is a valid position for tree placement.
 **/
val VALID_TREE_POS: (WorldGenLevel, BlockPos) -> Boolean = { world, pos ->
    TreeFeature.validTreePos(world, pos)
}

/**
 * Places a block at a given position in the world if the given [predicate] allows it.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param worldPlacer The function responsible for actually placing a block at a position (no default set).
 * @param predicate A function that returns `true` if the block can be placed at the position (no default set).
 * @param pos The position to attempt to place the block (no default set).
 * @param config The [BlockStateProvider] providing the block state to place (no default set).
 * @param random Random source for block variation (no default set).
 **/
fun placeProvidedBlock(
    level: WorldGenLevel,
    worldPlacer: BiConsumer<BlockPos, BlockState>,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    pos: BlockPos,
    config: BlockStateProvider,
    random: RandomSource
) {
    if (predicate(level, pos)) {
        worldPlacer.accept(pos, config.getState(random, pos))
    }
}

/**
 * Places a leaf block at a given position if the [predicate] allows it.
 *
 * Intended to be used on a given leaf block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param foliageSetter The foliage setter used to place leaf blocks (no default set).
 * @param predicate A function that returns `true` if the block can be placed at the position (no default set).
 * @param pos The position to attempt to place the block (no default set).
 * @param config The block state provider for the blocks (no default set).
 * @param random Random source for block variation (no default set).
 **/
fun placeLeaf(
    level: WorldGenLevel,
    foliageSetter: FoliagePlacer.FoliageSetter,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    pos: BlockPos,
    config: BlockStateProvider,
    random: RandomSource
) {
    if (predicate(level, pos)) {
        foliageSetter.set(pos, config.getState(random, pos))
    }
}

/**
 * Places a spheroid of leaf blocks centered at [centerPos] with specified radii and vertical bias.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param foliageSetter The foliage setter used to place leaf blocks (no default set).
 * @param predicate A function that returns `true` if the block can be placed at the position (no default set).
 * @param random Random source for block variation (no default set).
 * @param centerPos The center position of the spheroid (no default set).
 * @param xzRadius Horizontal radius of the spheroid (no default set).
 * @param yRadius Vertical radius of the spheroid (no default set).
 * @param verticalBias Vertical offset bias applied to shape the spheroid (no default set).
 * @param config The block state provider for the blocks (no default set).
 **/
fun placeSpheroid(
    level: WorldGenLevel,
    foliageSetter: FoliagePlacer.FoliageSetter,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    random: RandomSource,
    centerPos: BlockPos,
    xzRadius: Float,
    yRadius: Float,
    verticalBias: Float,
    config: BlockStateProvider
) {
    val xzRadiusSquared = xzRadius * xzRadius
    val yRadiusSquared = yRadius * yRadius
    val superRadiusSquared = xzRadiusSquared * yRadiusSquared

    placeLeaf(level, foliageSetter, predicate, centerPos, config, random)

    for (y in 0..yRadius.toInt()) {
        placeLeaf(level, foliageSetter, predicate, centerPos.offset(0, y, 0), config, random)
        placeLeaf(level, foliageSetter, predicate, centerPos.offset(0, -y, 0), config, random)
    }

    for (x in 0..xzRadius.toInt()) {
        for (z in 1..xzRadius.toInt()) {
            if (x * x + z * z > xzRadiusSquared) continue

            placeLeaf(level, foliageSetter, predicate, centerPos.offset(x, 0, z), config, random)
            placeLeaf(level, foliageSetter, predicate, centerPos.offset(-x, 0, -z), config, random)
            placeLeaf(level, foliageSetter, predicate, centerPos.offset(-z, 0, x), config, random)
            placeLeaf(level, foliageSetter, predicate, centerPos.offset(z, 0, -x), config, random)

            for (y in 1..yRadius.toInt()) {
                val xzSquare = (x * x + z * z) * yRadiusSquared

                if (xzSquare + ((y - verticalBias) * (y - verticalBias) * xzRadiusSquared) <= superRadiusSquared) {
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(x, y, z), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(-x, y, -z), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(-z, y, x), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(z, y, -x), config, random)
                }

                if (xzSquare + ((y + verticalBias) * (y + verticalBias) * xzRadiusSquared) <= superRadiusSquared) {
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(x, -y, z), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(-x, -y, -z), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(-z, -y, x), config, random)
                    placeLeaf(level, foliageSetter, predicate, centerPos.offset(z, -y, -x), config, random)
                }
            }
        }
    }
}

/**
 * Places a spheroid of blocks using a [BiConsumer] placer instead of a foliage setter.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param placer A [BiConsumer] responsible for placing blocks at positions (no default set).
 * @param predicate A function that returns `true` if the block can be placed at the position (no default set).
 * @param random Random source for block variation (no default set).
 * @param centerPos The center position of the spheroid (no default set).
 * @param xzRadius Horizontal radius of the spheroid (no default set).
 * @param yRadius Vertical radius of the spheroid (no default set).
 * @param config The block state provider for the blocks (no default set).
 */
fun placeSpheroid(
    level: WorldGenLevel,
    placer: BiConsumer<BlockPos, BlockState>,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    random: RandomSource, centerPos: BlockPos,
    xzRadius: Float,
    yRadius: Float,
    config: BlockStateProvider) {
    val placerLambda: (BlockPos, BlockState) -> Unit = { pos, state -> placer.accept(pos, state) }

    val xzRadiusSquared = xzRadius * xzRadius
    val yRadiusSquared = yRadius * yRadius
    val superRadiusSquared = xzRadiusSquared * yRadiusSquared

    placeProvidedBlock(level, placer, predicate, centerPos, config, random)

    for (y in 0..yRadius.toInt()) {
        placeProvidedBlock(level, placer, predicate, centerPos.offset(0, y, 0), config, random)
        placeProvidedBlock(level, placer, predicate, centerPos.offset(0, -y, 0), config, random)
    }

    for (x in 0..xzRadius.toInt()) {
        for (z in 1..xzRadius.toInt()) {
            if (x * x + z * z > xzRadiusSquared) continue

            placeProvidedBlock(level, placer, predicate, centerPos.offset(x, 0, z), config, random)
            placeProvidedBlock(level, placer, predicate, centerPos.offset(-x, 0, -z), config, random)
            placeProvidedBlock(level, placer, predicate, centerPos.offset(-z, 0, x), config, random)
            placeProvidedBlock(level, placer, predicate, centerPos.offset(z, 0, -x), config, random)

            for (y in 1..yRadius.toInt()) {
                val ySquare = y * y * xzRadiusSquared
                if ((x * x + z * z) * yRadiusSquared + ySquare <= superRadiusSquared) {
                    val edgeCheck = ((x + 1) * (x + 1) + z * z) * yRadiusSquared > superRadiusSquared &&
                            ((x * x + (z + 1) * (z + 1)) * yRadiusSquared > superRadiusSquared)

                    if (edgeCheck) {
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(x, y, z), config.getState(random, centerPos.offset(x, y, z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-x, y, -z), config.getState(random, centerPos.offset(-x, y, -z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-z, y, x), config.getState(random, centerPos.offset(-z, y, x)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(z, y, -x), config.getState(random, centerPos.offset(z, y, -x)))

                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(x, -y, z), config.getState(random, centerPos.offset(x, -y, z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-x, -y, -z), config.getState(random, centerPos.offset(-x, -y, -z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-z, -y, x), config.getState(random, centerPos.offset(-z, -y, x)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(z, -y, -x), config.getState(random, centerPos.offset(z, -y, -x)))
                        continue
                    }

                    placerLambda(centerPos.offset(x, y, z), config.getState(random, centerPos.offset(x, y, z)))
                    placerLambda(centerPos.offset(-x, y, -z), config.getState(random, centerPos.offset(-x, y, -z)))
                    placerLambda(centerPos.offset(-z, y, x), config.getState(random, centerPos.offset(-z, y, x)))
                    placerLambda(centerPos.offset(z, y, -x), config.getState(random, centerPos.offset(z, y, -x)))

                    placerLambda(centerPos.offset(x, -y, z), config.getState(random, centerPos.offset(x, -y, z)))
                    placerLambda(centerPos.offset(-x, -y, -z), config.getState(random, centerPos.offset(-x, -y, -z)))
                    placerLambda(centerPos.offset(-z, -y, x), config.getState(random, centerPos.offset(-z, -y, x)))
                    placerLambda(centerPos.offset(z, -y, -x), config.getState(random, centerPos.offset(z, -y, -x)))
                }
            }
        }
    }
}

/**
 * Traces and places root blocks along a given position path.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param rootPlacer The [RootPlacer] responsible for placing root blocks (no default set).
 * @param random Random source for block variation (no default set).
 * @param dirtRoot The block state provider for the root blocks (no default set).
 * @param posTracer Iterable of positions used to trace root placement (no default set).
 **/
fun traceRoot(
    level: WorldGenLevel,
    rootPlacer: RootPlacer,
    random: RandomSource,
    dirtRoot: BlockStateProvider,
    posTracer: Iterable<BlockPos>
) {
    for (rootPos in posTracer) {
        if (anyBelowMatch(rootPos, rootPlacer.rootPenetrability - 1) { blockPos ->
                level.isStateAtPosition(blockPos, ROOT_SHOULD_SKIP)
            }) return

        if (!placeIfValidRootPos(level, rootPlacer, random, rootPos, dirtRoot)) return
    }
}

/**
 * Traces and places exposed root blocks along a given position path.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param rootPlacer The [RootPlacer] responsible for placing root blocks (no default set).
 * @param random Random source for block variation (no default set).
 * @param exposedRoot The block state provider for the exposed root blocks (no default set).
 * @param dirtRoot The block state provider for the root blocks (no default set).
 * @param posTracer Iterable of positions used to trace root placement (no default set).
 **/
fun traceExposedRoot(
    level: WorldGenLevel,
    rootPlacer: RootPlacer,
    random: RandomSource,
    exposedRoot: BlockStateProvider,
    dirtRoot: BlockStateProvider,
    posTracer: Iterable<BlockPos>
) {
    for (exposedPos in posTracer) {
        if (level.isStateAtPosition(exposedPos, ROOT_SHOULD_SKIP)) continue

        if (hasEmptyNeighborExceptBelow(level, exposedPos)) {
            if (anyBelowMatch(exposedPos, rootPlacer.rootPenetrability - 1) { blockPos ->
                    !worldGenReplaceable(level.getBlockState(blockPos)) &&
                            level.getBlockState(blockPos) != exposedRoot.getState(random, exposedPos)
                }) return

            rootPlacer.placer.accept(exposedPos, exposedRoot.getState(random, exposedPos))
        } else {
            if (placeIfValidRootPos(level, rootPlacer, random, exposedPos, dirtRoot)) {
                traceRoot(level, rootPlacer, random, dirtRoot, posTracer)
            }
            return
        }
    }
}

/**
 * Places a root block at a given position in the world if the position is valid.
 *
 * Intended to be used on a given block and position within the world.
 *
 * @param level The world in which to place the blocks (no default set).
 * @param rootPlacer The [RootPlacer] responsible for placing root blocks (no default set).
 * @param random Random source for block variation (no default set).
 * @param pos The position to attempt to place the block (no default set).
 * @param config The [BlockStateProvider] providing the block state to place (no default set).
 **/
fun placeIfValidRootPos(
    level: WorldGenLevel,
    rootPlacer: RootPlacer,
    random: RandomSource,
    pos: BlockPos,
    config: BlockStateProvider
): Boolean {
    return if (!anyBelowMatch(pos, rootPlacer.rootPenetrability - 1) { blockPos ->
            !canRootGrowIn(level, blockPos)
        }) {
        rootPlacer.placer.accept(pos, config.getState(random, pos))
        true
    } else {
        false
    }
}