@file:Suppress("unused")

package io.autumn.carminite.tree.feature

import com.mojang.serialization.Codec
import io.autumn.carminite.feature.RootPlacer
import io.autumn.carminite.feature.VALID_TREE_POS
import io.autumn.carminite.feature.placeBresenhamBranch
import io.autumn.carminite.feature.hasAirAround
import io.autumn.carminite.feature.placeIfValidRootPos
import io.autumn.carminite.feature.placeIfValidTreePos
import io.autumn.carminite.feature.placeSpheroid
import io.autumn.carminite.feature.placeExposedRoot
import io.autumn.carminite.feature.translate
import io.autumn.carminite.math.VoxelBresenhamIterator
import io.autumn.carminite.tree.config.CarminiteTreeFeatureConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.VineBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer
import kotlin.math.max

abstract class HollowTreeFeature(config: Codec<CarminiteTreeFeatureConfig>) : CarminiteTreeFeature(config) {

    companion object {
        private const val LEAF_DUNGEON_CHANCE = 8

        fun makeHollowTree(
            world: WorldGenLevel,
            random: RandomSource,
            pos: BlockPos,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            decorationPlacer: RootPlacer,
            config: CarminiteTreeFeatureConfig
        ) {
            val radius = random.nextInt(4) + 1
            val height = random.nextInt(64) + 32

            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 3, 2, 6, 0.75, 3, 5, 3, false, config)
            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 8, 0.9, 3, 5, 3, false, config)

            buildTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius, height, config)

            // fireflies
            /*
            repeat(random.nextInt(6 * radius) + 5) {
                val fHeight = (height * random.nextDouble() * 0.9).toInt() + (height / 10)
                val fAngle = random.nextDouble()
                addBug(world, TFBlocks.FIREFLY.get(), pos, radius, fHeight, fAngle)
            }

            // cicadas
            repeat(random.nextInt(3 * radius) + 5) {
                val fHeight = (height * random.nextDouble() * 0.9).toInt() + (height / 10)
                val fAngle = random.nextDouble()
                addBug(world, TFBlocks.CICADA.get(), pos, radius, fHeight, fAngle)
            }*/

            buildFullCrown(world, trunkPlacer, leavesPlacer, random, pos, radius, height, config)

            repeat(random.nextInt(3) + 3) {
                val branchHeight = (height * random.nextDouble() * 0.9).toInt() + (height / 10)
                val branchRotation = random.nextDouble()
                makeSmallBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, branchHeight, 4.toDouble(), branchRotation, 0.35, true, config)
            }
        }

        fun buildFullCrown(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            pos: BlockPos,
            radius: Int,
            height: Int,
            config: CarminiteTreeFeatureConfig
        ) {
            val crownRadius = radius * 4 + 4
            val bvar = radius + 2

            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - crownRadius, 0, crownRadius, 0.35, bvar, bvar + 2, 2, true, config)
            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - crownRadius / 2, 0, crownRadius, 0.28, bvar, bvar + 2, 1, true, config)
            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius, 0.15, 2, 4, 2, true, config)
            buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius / 2, 0.05, bvar, bvar + 2, 1, true, config)
        }

        fun buildBranchRing(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            pos: BlockPos,
            radius: Int,
            branchHeight: Int,
            heightVar: Int,
            length: Int,
            tilt: Double,
            minBranches: Int,
            maxBranches: Int,
            size: Int,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val numBranches = random.nextInt(maxBranches - minBranches) + minBranches
            val branchRotation = 1.0 / (numBranches + 1)
            val branchOffset = random.nextDouble()

            for (i in 0..numBranches) {
                val dHeight = if (heightVar > 0) branchHeight - heightVar + random.nextInt(2 * heightVar) else branchHeight

                when (size) {
                    2 -> makeLargeBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length.toDouble(), i * branchRotation + branchOffset, tilt, leafy, config)
                    1 -> makeMedBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length.toDouble(), i * branchRotation + branchOffset, tilt, leafy, config)
                    3 -> makeRoot(world, random, pos, radius, dHeight, length.toDouble(), i * branchRotation + branchOffset, tilt, config)
                    else -> makeSmallBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length.toDouble(), i * branchRotation + branchOffset, tilt, leafy, config)
                }
            }
        }

        fun buildTrunk(
            level: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            decoPlacer: RootPlacer,
            random: RandomSource,
            pos: BlockPos,
            radius: Int,
            height: Int,
            config: CarminiteTreeFeatureConfig
        ) {
            val hollow = radius shr 1

            for (dx in -radius..radius) {
                for (dz in -radius..radius) {
                    val ax = kotlin.math.abs(dx)
                    val az = kotlin.math.abs(dz)
                    val dist = max(ax, az) + (kotlin.math.min(ax, az) shr 1)

                    if (dist > radius) continue

                    for (dy in -4 until 0) {
                        val dPos = pos.offset(dx, dy, dz)
                        if (hasAirAround(level, dPos)) {
                            if (dist > hollow) trunkPlacer.accept(dPos, config.trunkProvider.getState(level, random, dPos))
                            else trunkPlacer.accept(dPos, config.branchProvider.getState(level, random, dPos))
                        } else {
                            placeIfValidRootPos(level, decoPlacer, random, dPos, config.rootsProvider)
                        }
                    }
                }
            }

            for (dx in -radius..radius) {
                for (dz in -radius..radius) {
                    for (dy in 0..height) {
                        val dPos = pos.offset(dx, dy, dz)
                        val ax = kotlin.math.abs(dx)
                        val az = kotlin.math.abs(dz)
                        val dist = (max(ax, az) + (minOf(ax, az) * 0.5)).toInt()

                        if (dist in (hollow + 1)..radius) placeIfValidTreePos(level, trunkPlacer, random, dPos, config.trunkProvider)
                        if (dist == hollow && dx == hollow) level.setBlock(dPos, Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true), 3)
                    }
                }
            }
        }

        fun addBug(level: WorldGenLevel, bug: Block, pos: BlockPos, diameter: Int, fHeight: Int, fAngle: Double) {
            var angle = fAngle % 1.0
            var facing = Direction.EAST
            when {
                angle > 0.875 || angle <= 0.125 -> facing = Direction.SOUTH
                angle > 0.375 && angle <= 0.625 -> facing = Direction.NORTH
                angle > 0.625 -> facing = Direction.WEST
            }

            val src = translate(pos.above(fHeight), (diameter + 1).toDouble(), angle, 0.5)
            if (bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing).canSurvive(level, src)) {
                level.setBlock(src, bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing), 3)
            }
        }

        fun makeSmallBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            src: BlockPos,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val dest = translate(src, length, angle, tilt)
            placeBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider)

            if (leafy) {
                val leafRad = random.nextInt(2) + 1.5f
                placeSpheroid(world, leavesPlacer, VALID_TREE_POS, random, dest, leafRad, leafRad, config.leavesProvider)
            }
        }

        fun makeSmallBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            pos: BlockPos,
            diameter: Int,
            branchHeight: Int,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val src = translate(pos.above(branchHeight), diameter.toDouble(), angle, 0.5)
            makeSmallBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config)
        }

        fun makeMedBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            src: BlockPos,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val dest = translate(src, length, angle, tilt)
            placeBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider)

            if (leafy) placeSpheroid(world, leavesPlacer, VALID_TREE_POS, random, dest, 2.5f, 2.5f, config.leavesProvider)

            val numShoots = random.nextInt(2) + 1
            val angleInc = 0.8 / numShoots

            repeat(numShoots + 1) { i ->
                val angleVar = (angleInc * i) - 0.4
                val outVar = random.nextDouble() * 0.8 + 0.2
                val tiltVar = random.nextDouble() * 0.75 + 0.15
                val bsrc = translate(src, length * outVar, angle, tilt)
                makeSmallBranch(world, trunkPlacer, leavesPlacer, random, bsrc, length * 0.4, angle + angleVar, tilt * tiltVar, leafy, config)
            }
        }

        fun makeMedBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            pos: BlockPos,
            diameter: Int,
            branchHeight: Int,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val src = translate(pos.above(branchHeight), diameter.toDouble(), angle, 0.5)
            makeMedBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config)
        }

        fun makeLargeBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            src: BlockPos,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val dest = translate(src, length, angle, tilt)
            placeBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider)

            repeat(random.nextInt(3) + 1) { i ->
                val vx = if (i and 2 == 0) 1 else 0
                val vy = if (i and 1 == 0) 1 else -1
                val vz = if (i and 2 == 0) 0 else 1
                placeBresenhamBranch(world, trunkPlacer, random, src.offset(vx, vy, vz), dest, config.branchProvider)
            }

            if (leafy) placeSpheroid(world, leavesPlacer, VALID_TREE_POS, random, dest.above(), 3.5f, 3.5f, config.leavesProvider)

            val numMedBranches = random.nextInt(maxOf((length / 6).toInt(), 1)) + random.nextInt(2) + 1
            repeat(numMedBranches + 1) { i ->
                val outVar = random.nextDouble() * 0.3 + 0.3
                val angleVar = random.nextDouble() * 0.225 * if (i and 1 == 0) 1.0 else -1.0
                val bsrc = translate(src, length * outVar, angle, tilt)
                makeMedBranch(world, trunkPlacer, leavesPlacer, random, bsrc, length * 0.6, angle + angleVar, tilt, leafy, config)
            }

            val numSmallBranches = random.nextInt(2) + 1
            repeat(numSmallBranches + 1) { i ->
                val outVar = random.nextDouble() * 0.25 + 0.25
                val angleVar = random.nextDouble() * 0.25 * if (i and 1 == 0) 1.0 else -1.0
                val bsrc = translate(src, length * outVar, angle, tilt)
                makeSmallBranch(world, trunkPlacer, leavesPlacer, random, bsrc, max(length * 0.3, 2.0), angle + angleVar, tilt, leafy, config)
            }

            //if (random.nextInt(LEAF_DUNGEON_CHANCE) == 0) makeLeafDungeon(world, leavesPlacer, random, dest.above(), config)
        }

        fun makeLargeBranch(
            world: WorldGenLevel,
            trunkPlacer: BiConsumer<BlockPos, BlockState>,
            leavesPlacer: BiConsumer<BlockPos, BlockState>,
            random: RandomSource,
            pos: BlockPos,
            radius: Int,
            branchHeight: Int,
            length: Double,
            angle: Double,
            tilt: Double,
            leafy: Boolean,
            config: CarminiteTreeFeatureConfig
        ) {
            val src = translate(pos.above(branchHeight), radius.toDouble(), angle, 0.5)
            makeLargeBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config)
        }

        fun makeRoot(
            world: WorldGenLevel,
            random: RandomSource,
            pos: BlockPos,
            diameter: Int,
            branchHeight: Int,
            length: Double,
            angle: Double,
            tilt: Double,
            config: CarminiteTreeFeatureConfig
        ) {
            val src = translate(pos.above(branchHeight), diameter.toDouble(), angle, 0.5)
            val dest = translate(src, length, angle, tilt)
            placeExposedRoot(world, RootPlacer({ checkedPos, state ->
                world.setBlock(checkedPos, state, 3)
                world.setBlock(checkedPos.below(), state, 3)
            }, 2), random, config.branchProvider, config.rootsProvider, VoxelBresenhamIterator(src, dest))
        }
    }
}