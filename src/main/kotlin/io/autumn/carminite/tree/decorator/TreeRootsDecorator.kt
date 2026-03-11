@file:Suppress("unused")

package io.autumn.carminite.tree.decorator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.feature.RootPlacer
import io.autumn.carminite.feature.placeExposedRoot
import io.autumn.carminite.feature.placeRoot
import io.autumn.carminite.feature.translate
import io.autumn.carminite.math.VoxelBresenhamIterator
import io.autumn.carminite.tree.TreeUtilRegistry
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import java.util.Optional
import java.util.function.BiConsumer

class TreeRootsDecorator private constructor(
    val strands: Int,
    val addExtraStrands: Int,
    val length: Int,
    val yOffset: Int,
    val surfaceBlock: BlockStateProvider,
    val rootBlock: BlockStateProvider,
    val rootPenetrability: Int,
    val hasSurfaceRoots: Boolean
) : TreeDecorator() {
    companion object {
        private val EMPTY: SimpleStateProvider = BlockStateProvider.simple(Blocks.AIR.defaultBlockState())

        val CODEC: MapCodec<TreeRootsDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.intRange(0, 16).fieldOf("base_strand_count").forGetter { it.strands },
                Codec.intRange(0, 16).fieldOf("additional_random_strands").forGetter { it.addExtraStrands },
                Codec.intRange(0, 32).fieldOf("root_length").forGetter { it.length },
                Codec.INT.fieldOf("y_offset").forGetter { it.yOffset },
                BlockStateProvider.CODEC.optionalFieldOf("exposed_roots_provider")
                    .forGetter { Optional.ofNullable(if (it.surfaceBlock != EMPTY) it.surfaceBlock else null) },
                BlockStateProvider.CODEC.fieldOf("ground_roots_provider").forGetter { it.rootBlock },
                Codec.INT.fieldOf("rootPenetrability").forGetter { it.rootPenetrability }
            ).apply(instance) { count, addExtraStrands, length, yOffset, surfaceBlockOpt, rootBlock, rootPenetrability ->
                TreeRootsDecorator(
                    count,
                    addExtraStrands,
                    length,
                    yOffset,
                    surfaceBlockOpt.orElse(EMPTY) as BlockStateProvider,
                    rootBlock,
                    rootPenetrability,
                    surfaceBlockOpt.isPresent
                )
            }
        }
    }

    constructor(
        count: Int,
        addExtraStrands: Int,
        length: Int,
        rootBlock: BlockStateProvider,
        rootPenetrability: Int
    ) : this(
        count,
        addExtraStrands,
        length,
        0,
        EMPTY,
        rootBlock,
        rootPenetrability,
        false
    )

    constructor(
        count: Int,
        addExtraStrands: Int,
        length: Int,
        yOffset: Int,
        surfaceBlock: BlockStateProvider,
        rootBlock: BlockStateProvider,
        rootPenetrability: Int
    ) : this(
        count,
        addExtraStrands,
        length,
        yOffset,
        surfaceBlock,
        rootBlock,
        rootPenetrability,
        true
    )

    override fun type(): TreeDecoratorType<TreeRootsDecorator> = TreeUtilRegistry.TREE_ROOTS_DECORATOR

    override fun place(
        context: Context
    ) {
        if (context.logs().isEmpty) return

        val numBranches = strands + context.random().nextInt(addExtraStrands + 1)
        val offset = context.random().nextFloat()
        val startPos = context.logs().first().above(yOffset)

        if (hasSurfaceRoots) {
            repeat(numBranches) { i ->
                val dest = translate(startPos.below(i + 2), length.toDouble(), 0.3 * i + offset, 0.8)
                placeExposedRoot(
                    context.level(),
                    RootPlacer(BiConsumer(context::setBlock), rootPenetrability),
                    context.random(),
                    surfaceBlock,
                    rootBlock,
                    VoxelBresenhamIterator(startPos.below(), dest)
                )
            }
        } else {
            repeat(numBranches) { i ->
                val dest = translate(startPos.below(i + 2), length.toDouble(), 0.3 * i + offset, 0.8)
                placeRoot(
                    context.level(),
                    RootPlacer(BiConsumer(context::setBlock), rootPenetrability),
                    context.random(),
                    rootBlock,
                    VoxelBresenhamIterator(startPos.below(), dest)
                )
            }
        }
    }
}