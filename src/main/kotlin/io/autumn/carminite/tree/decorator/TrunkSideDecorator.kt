package io.autumn.carminite.tree.decorator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.tree.TreeUtilRegistry
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

class TrunkSideDecorator(
    private val count: Int,
    private val probability: Float,
    private val decoration: BlockStateProvider
): TreeDecorator() {
    companion object {
        val CODEC: MapCodec<TrunkSideDecorator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.intRange(0, 64).fieldOf("placement_count").forGetter { it.count },
                Codec.floatRange(0f, 1f).fieldOf("probability_of_placement").forGetter { it.probability },
                BlockStateProvider.CODEC.fieldOf("deco_provider").forGetter { it.decoration }
            ).apply(instance, ::TrunkSideDecorator)
        }
    }

    override fun type(): TreeDecoratorType<*> = TreeUtilRegistry.TRUNK_SIDE_DECORATOR

    override fun place(
        context: Context
    ) {
        val logs = context.logs()
        val blockCount = logs.size

        if (blockCount == 0) {
            return
        }

        repeat(count) {
            if (context.random().nextFloat() >= probability) return@repeat

            val logPos = logs[context.random().nextInt(blockCount)]
            val direction = Direction.Plane.HORIZONTAL.getRandomDirection(context.random())
            val newPos = logPos.offset(direction.stepX, 0, direction.stepZ)

            if (context.isAir(newPos)) {
                val state = decoration.getState(context.level(),context.random(), newPos)
                val finalState = if (state.hasProperty(BlockStateProperties.FACING)) {
                    state.setValue(BlockStateProperties.FACING, direction)
                } else {
                    state
                }
                context.setBlock(newPos, finalState)
            }
        }
    }
}
