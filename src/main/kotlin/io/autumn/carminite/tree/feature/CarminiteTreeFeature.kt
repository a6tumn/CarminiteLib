package io.autumn.carminite.tree.feature

import com.google.common.collect.Iterables
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import io.autumn.carminite.feature.RootPlacer
import io.autumn.carminite.tree.config.CarminiteTreeFeatureConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape
import net.minecraft.world.phys.shapes.DiscreteVoxelShape
import java.util.function.BiConsumer

abstract class CarminiteTreeFeature(configIn: Codec<CarminiteTreeFeatureConfig>) : Feature<CarminiteTreeFeatureConfig>(configIn) {

    override fun place(context: FeaturePlaceContext<CarminiteTreeFeatureConfig>): Boolean {
        val world = context.level()
        val random = context.random()
        val pos = context.origin()
        val config = context.config()

        val set = Sets.newHashSet<BlockPos>()
        val set1 = Sets.newHashSet<BlockPos>()
        val set2 = Sets.newHashSet<BlockPos>()
        val set3 = Sets.newHashSet<BlockPos>()

        val trunkPlacer = BiConsumer<BlockPos, BlockState> { p, s ->
            set.add(p.immutable())
            world.setBlock(p, s, Block.UPDATE_KNOWN_SHAPE or Block.UPDATE_ALL)
        }

        val leavesPlacer = BiConsumer<BlockPos, BlockState> { p, s ->
            set1.add(p.immutable())
            world.setBlock(p, s, Block.UPDATE_KNOWN_SHAPE or Block.UPDATE_ALL)
        }

        val rootPlacerConsumer = BiConsumer<BlockPos, BlockState> { p, s ->
            set2.add(p.immutable())
            world.setBlock(p, s, Block.UPDATE_KNOWN_SHAPE or Block.UPDATE_ALL)
        }

        val decoratorPlacer = BiConsumer<BlockPos, BlockState> { p, s ->
            set3.add(p.immutable())
            world.setBlock(p, s, Block.UPDATE_KNOWN_SHAPE or Block.UPDATE_ALL)
        }

        val success = generate(
            world,
            random,
            pos,
            trunkPlacer,
            leavesPlacer,
            RootPlacer(rootPlacerConsumer, 1),
            config
        )

        if (success && (set1.isNotEmpty() || set2.isNotEmpty())) {

            if (config.decorators.isNotEmpty()) {
                val decoratorContext = TreeDecorator.Context(
                    world,
                    decoratorPlacer,
                    random,
                    set1,
                    set2,
                    set
                )

                config.decorators.forEach {
                    it.place(decoratorContext)
                }
            }

            return BoundingBox.encapsulatingPositions(
                Iterables.concat(set, set1, set2, set3)
            ).map { boundingBox ->

                val shape: DiscreteVoxelShape =
                    updateLeaves(world, boundingBox, set1, set3, set)

                StructureTemplate.updateShapeAtEdge(
                    world,
                    3,
                    shape,
                    boundingBox.minX(),
                    boundingBox.minY(),
                    boundingBox.minZ()
                )

                true
            }.orElse(false)!!

        } else {
            return false
        }
    }

    fun updateLeaves(
        level: WorldGenLevel,
        bounds: BoundingBox,
        logs: Set<BlockPos>,
        decorationSet: Set<BlockPos>,
        rootPositions: Set<BlockPos>
    ): DiscreteVoxelShape {

        val shape = BitSetDiscreteVoxelShape(bounds.xSpan, bounds.ySpan, bounds.zSpan)
        val maxDistance = 7
        val toCheck = MutableList<MutableSet<BlockPos>>(maxDistance) { mutableSetOf() }

        (decorationSet union rootPositions).forEach { pos ->
            if (bounds.isInside(pos)) {
                shape.fill(pos.x - bounds.minX(), pos.y - bounds.minY(), pos.z - bounds.minZ())
            }
        }

        val neighborPos = BlockPos.MutableBlockPos()
        var smallestDistance = 0
        toCheck[0].addAll(logs)

        while (true) {
            while (smallestDistance < maxDistance && toCheck[smallestDistance].isNotEmpty()) {
                val pos = toCheck[smallestDistance].first()
                toCheck[smallestDistance].remove(pos)

                if (bounds.isInside(pos)) {
                    if (smallestDistance != 0) {
                        val state = level.getBlockState(pos)
                        level.setBlock(pos, state.setValue(BlockStateProperties.DISTANCE, smallestDistance), Block.UPDATE_KNOWN_SHAPE or Block.UPDATE_ALL)
                    }

                    shape.fill(pos.x - bounds.minX(), pos.y - bounds.minY(), pos.z - bounds.minZ())

                    for (direction in Direction.entries) {
                        neighborPos.set(pos.offset(direction.unitVec3i))
                        if (bounds.isInside(neighborPos)) {
                            val xInShape = neighborPos.x - bounds.minX()
                            val yInShape = neighborPos.y - bounds.minY()
                            val zInShape = neighborPos.z - bounds.minZ()

                            if (!shape.isFull(xInShape, yInShape, zInShape)) {
                                val currentState = level.getBlockState(neighborPos)
                                val distanceOpt = LeavesBlock.getOptionalDistanceAt(currentState)
                                if (distanceOpt.isPresent) {
                                    val newDistance = minOf(distanceOpt.getAsInt(), smallestDistance + 1)
                                    if (newDistance < maxDistance) {
                                        toCheck[newDistance].add(neighborPos.immutable())
                                        smallestDistance = minOf(smallestDistance, newDistance)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (smallestDistance >= maxDistance) break
            smallestDistance++
        }

        return shape
    }

    protected abstract fun generate(
        world: WorldGenLevel,
        random: RandomSource,
        pos: BlockPos,
        trunkPlacer: BiConsumer<BlockPos, BlockState>,
        leavesPlacer: BiConsumer<BlockPos, BlockState>,
        decorationPlacer: RootPlacer,
        config: CarminiteTreeFeatureConfig
    ): Boolean
}