@file:Suppress("unused")

package io.autumn.carminite.tree.trunkplacers

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.feature.translate
import io.autumn.carminite.math.VoxelBresenhamIterator
import io.autumn.carminite.tree.TreeUtilRegistry
import io.autumn.carminite.tree.trunkplacers.config.BranchesConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.TreeFeature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer

class MegaTrunkPlacer(
    baseHeight: Int,
    randomHeightA: Int,
    randomHeightB: Int,
    val branchDownwardOffset: Int,
    val branchesConfig: BranchesConfig,
    val perpendicularBranches: Boolean,
    val preventExposedRoot: Boolean
) : TrunkPlacer(baseHeight, randomHeightA, randomHeightB) {
    companion object {
        val CODEC: MapCodec<MegaTrunkPlacer> = RecordCodecBuilder.mapCodec { instance ->
            trunkPlacerParts(instance).and(
                instance.group(
                    Codec.intRange(0, 24)
                        .fieldOf("branch_start_offset_down")
                        .forGetter { it.branchDownwardOffset },
                    BranchesConfig.CODEC
                        .fieldOf("branch_config")
                        .forGetter { it.branchesConfig },
                    Codec.BOOL
                        .fieldOf("perpendicular_branches")
                        .forGetter { it.perpendicularBranches },
                    Codec.BOOL
                        .fieldOf("prevent_exposed_root")
                        .forGetter { it.preventExposedRoot }
                )
            ).apply(instance, ::MegaTrunkPlacer)
        }
    }

    override fun type(): TrunkPlacerType<*> = TreeUtilRegistry.MEGA_TRUNK_PLACER

    override fun placeTrunk(
        level: WorldGenLevel,
        trunkSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        treeHeight: Int,
        origin: BlockPos,
        config: TreeConfiguration
    ): List<FoliagePlacer.FoliageAttachment> {
        val leafAttachments = mutableListOf<FoliagePlacer.FoliageAttachment>()
        var adjustedHeight = treeHeight
        val numBranches = branchesConfig.branchCount + random.nextInt(branchesConfig.randomAddBranches + 1)
        val offset = random.nextFloat()

        if (preventExposedRoot) {
            for (direction in Direction.Plane.HORIZONTAL) {
                val belowPos = origin.below().relative(direction)
                if (level.isStateAtPosition(belowPos, BlockBehaviour.BlockStateBase::canBeReplaced)) {
                    for (x in -1..1) {
                        for (z in -1..1) {
                            val root = origin.below().offset(x, 0, z)
                            trunkSetter.accept(
                                root,
                                config.trunkProvider.getState(level, random, root)
                            )
                        }
                    }
                    break
                }
            }
        }

        for (y in 0..treeHeight) {
            var placedAny = false

            for (x in -1..1) {
                for (z in -1..1) {
                    val pos = origin.offset(x, y, z)
                    if (placeLog(level, trunkSetter, random, pos, config)) {
                        placedAny = true
                    }
                }
            }

            if (!placedAny) {
                adjustedHeight = y
                break
            }
        }

        leafAttachments.add(FoliagePlacer.FoliageAttachment(origin.above(adjustedHeight), 0, false))

        for (b in 0 until numBranches) {
            buildBranch(
                level, trunkSetter, origin, leafAttachments,
                adjustedHeight - branchDownwardOffset + b,
                branchesConfig.length,
                branchesConfig.spacingYaw * b + offset,
                branchesConfig.downwardsPitch,
                random,
                perpendicularBranches
            )
        }

        return leafAttachments
    }

    private fun buildBranch(
        level: WorldGenLevel,
        worldPlacer: BiConsumer<BlockPos, BlockState>,
        pos: BlockPos,
        leafBlocks: MutableList<FoliagePlacer.FoliageAttachment>,
        height: Int,
        length: Double,
        angle: Double,
        tilt: Double,
        treeRNG: RandomSource,
        perpendicularBranches: Boolean
    ) {
        val src = pos.above(height)
        val dest = translate(src, length, angle, tilt)

        if (perpendicularBranches) {
            drawBresenhamBranch(level, worldPlacer, treeRNG, src, BlockPos(dest.x, src.y, dest.z))

            val maxY = maxOf(src.y, dest.y)
            for (i in minOf(src.y, dest.y)..maxY) {
                placeWood(level, worldPlacer, treeRNG, BlockPos(dest.x, i, dest.z))
            }
        } else {
            drawBresenhamBranch(level, worldPlacer, treeRNG, src, dest)
        }

        placeWood(level, worldPlacer, treeRNG, dest.east())
        placeWood(level, worldPlacer, treeRNG, dest.west())
        placeWood(level, worldPlacer, treeRNG, dest.south())
        placeWood(level, worldPlacer, treeRNG, dest.north())

        leafBlocks.add(FoliagePlacer.FoliageAttachment(dest, 0, false))
    }

    private fun drawBresenhamBranch(
        level: WorldGenLevel,
        worldPlacer: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        from: BlockPos, to: BlockPos
    ) {
        for (pixel in VoxelBresenhamIterator(from, to)) {
            placeWood(level, worldPlacer, random, pixel)
        }
    }

    private fun placeWood(
        level: WorldGenLevel,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: BlockPos
    ): Boolean =
        placeWood(level, blockSetter, random, pos) { it }

    private fun placeWood(
        level: WorldGenLevel,
        blockSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        pos: BlockPos,
        propertySetter: (BlockState) -> BlockState
    ): Boolean {
        return if (TreeFeature.validTreePos(level, pos)) {
            blockSetter.accept(pos, propertySetter(branchesConfig.branchProvider.getState(level, random, pos)))
            true
        } else false
    }
}