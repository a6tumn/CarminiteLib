@file:Suppress("unused")

package io.autumn.carminite.tree.trunkplacer

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.tree.TreeUtilRegistry
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import java.util.function.BiConsumer

class TrunkRiser(
    val offset: Int,
    baseHeight: Int,
    heightRandA: Int,
    heightRandB: Int,
    val placer: TrunkPlacer
) : TrunkPlacer(
    baseHeight,
    heightRandA,
    heightRandB
) {
    companion object {
        val CODEC: MapCodec<TrunkRiser> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.intRange(0, 16)
                    .fieldOf("offset_up")
                    .forGetter { it.offset },
                Codec.intRange(0, 32)
                    .fieldOf("base_height")
                    .forGetter { it.baseHeight },
                Codec.intRange(0, 24)
                    .fieldOf("height_rand_a")
                    .forGetter { it.heightRandA },
                Codec.intRange(0, 24)
                    .fieldOf("height_rand_b")
                    .forGetter { it.heightRandB },
                TrunkPlacer.CODEC
                    .fieldOf("trunk_placer")
                    .forGetter { it.placer }
            ).apply(instance, ::TrunkRiser)
        }
    }

    override fun type(): TrunkPlacerType<TrunkRiser> {
        return TreeUtilRegistry.TRUNK_RISER
    }

    override fun placeTrunk(
        level: WorldGenLevel,
        trunkSetter: BiConsumer<BlockPos, BlockState>,
        random: RandomSource,
        treeHeight: Int,
        origin: BlockPos,
        treeConfig: TreeConfiguration
    ): List<FoliagePlacer.FoliageAttachment> {
        return placer.placeTrunk(
            level,
            trunkSetter,
            random,
            treeHeight,
            origin.above(offset),
            treeConfig
        )
    }
}