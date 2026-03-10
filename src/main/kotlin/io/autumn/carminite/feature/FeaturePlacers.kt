@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import java.util.function.BiConsumer

val VALID_TREE_POS: (WorldGenLevel, BlockPos) -> Boolean = { world, pos ->
    TreeFeature.validTreePos(world, pos)
}

fun placeProvidedBlock(
    level: WorldGenLevel,
    worldPlacer: BiConsumer<BlockPos, BlockState>,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    pos: BlockPos,
    config: BlockStateProvider,
    random: RandomSource
) {
    if (predicate(level, pos)) {
        worldPlacer.accept(pos, config.getState(level, random, pos))
    }
}

fun placeLeaf(
    level: WorldGenLevel,
    foliageSetter: FoliagePlacer.FoliageSetter,
    predicate: (WorldGenLevel, BlockPos) -> Boolean,
    pos: BlockPos,
    config: BlockStateProvider,
    random: RandomSource
) {
    if (predicate(level, pos)) {
        foliageSetter.set(pos, config.getState(level, random, pos))
    }
}

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
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(x, y, z), config.getState(level, random, centerPos.offset(x, y, z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-x, y, -z), config.getState(level, random, centerPos.offset(-x, y, -z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-z, y, x), config.getState(level, random, centerPos.offset(-z, y, x)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(z, y, -x), config.getState(level, random, centerPos.offset(z, y, -x)))

                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(x, -y, z), config.getState(level, random, centerPos.offset(x, -y, z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-x, -y, -z), config.getState(level, random, centerPos.offset(-x, -y, -z)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(-z, -y, x), config.getState(level, random, centerPos.offset(-z, -y, x)))
                        if (random.nextInt(3) != 0) placerLambda(centerPos.offset(z, -y, -x), config.getState(level, random, centerPos.offset(z, -y, -x)))
                        continue
                    }

                    placerLambda(centerPos.offset(x, y, z), config.getState(level, random, centerPos.offset(x, y, z)))
                    placerLambda(centerPos.offset(-x, y, -z), config.getState(level, random, centerPos.offset(-x, y, -z)))
                    placerLambda(centerPos.offset(-z, y, x), config.getState(level, random, centerPos.offset(-z, y, x)))
                    placerLambda(centerPos.offset(z, y, -x), config.getState(level, random, centerPos.offset(z, y, -x)))

                    placerLambda(centerPos.offset(x, -y, z), config.getState(level, random, centerPos.offset(x, -y, z)))
                    placerLambda(centerPos.offset(-x, -y, -z), config.getState(level, random, centerPos.offset(-x, -y, -z)))
                    placerLambda(centerPos.offset(-z, -y, x), config.getState(level, random, centerPos.offset(-z, -y, x)))
                    placerLambda(centerPos.offset(z, -y, -x), config.getState(level, random, centerPos.offset(z, -y, -x)))
                }
            }
        }
    }
}

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
                            level.getBlockState(blockPos) != exposedRoot.getState(level, random, exposedPos)
                }) return

            rootPlacer.placer.accept(exposedPos, exposedRoot.getState(level, random, exposedPos))
        } else {
            if (placeIfValidRootPos(level, rootPlacer, random, exposedPos, dirtRoot)) {
                traceRoot(level, rootPlacer, random, dirtRoot, posTracer)
            }
            return
        }
    }
}

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
        rootPlacer.placer.accept(pos, config.getState(level, random, pos))
        true
    } else {
        false
    }
}