package io.autumn.carminite.tree.decorator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.tree.TreeUtilRegistry
import net.minecraft.core.BlockPos
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class BlockInsertionDecorator(
    private val blockPos: Int,
    private val block: BlockStateProvider
): TreeDecorator() {
    companion object {
        val CODEC: MapCodec<BlockInsertionDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.intRange(0, 20)
                    .fieldOf("block_position")
                    .forGetter { it.blockPos },
                BlockStateProvider.CODEC
                    .fieldOf("deco_provider")
                    .forGetter { it.block }
            ).apply(instance, ::BlockInsertionDecorator)
        }
    }

    override fun type(): TreeDecoratorType<*> = TreeUtilRegistry.BLOCK_INSERTION_DECORATOR

    override fun place(
        context: Context
    ) {
        val pos: BlockPos = context.logs()[0].offset(0, blockPos, 0)
        context.setBlock(pos, block.getState(context.level(), context.random(), pos))
    }
}