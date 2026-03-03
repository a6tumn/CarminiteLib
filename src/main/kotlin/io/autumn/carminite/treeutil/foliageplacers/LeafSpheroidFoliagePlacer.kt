package io.autumn.carminite.treeutil.foliageplacers

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.autumn.carminite.featureutil.VALID_TREE_POS
import io.autumn.carminite.featureutil.placeLeaf
import io.autumn.carminite.featureutil.placeSpheroid
import io.autumn.carminite.treeutil.TreeUtilRegistry
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

class LeafSpheroidFoliagePlacer(
    val horizontalRadius: Float,
    val verticalRadius: Float,
    val yOffset: IntProvider,
    val randomHorizontal: Int,
    val randomVertical: Int,
    val verticalBias: Float,
    val shagFactor: Int
) : FoliagePlacer(
    ConstantInt.of(horizontalRadius.toInt()), yOffset) {
    companion object {
        val CODEC: MapCodec<LeafSpheroidFoliagePlacer> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.floatRange(0f, 16f).fieldOf("horizontal_radius").forGetter { it.horizontalRadius },
                Codec.floatRange(0f, 16f).fieldOf("vertical_radius").forGetter { it.verticalRadius },
                IntProvider.codec(0, 8).fieldOf("offset").forGetter { it.yOffset },
                Codec.intRange(0, 16).fieldOf("random_add_horizontal").orElse(0).forGetter { it.randomHorizontal },
                Codec.intRange(0, 16).fieldOf("random_add_vertical").orElse(0).forGetter { it.randomVertical },
                Codec.floatRange(-0.5f, 0.5f).fieldOf("vertical_filler_bias").orElse(0f).forGetter { it.verticalBias },
                Codec.intRange(0, 256).fieldOf("shag_factor").orElse(0).forGetter { it.shagFactor }
            ).apply(instance, ::LeafSpheroidFoliagePlacer)
        }

        val NO_OP = LeafSpheroidFoliagePlacer(0f, 0f, ConstantInt.of(0), 0, 0, 0f, 0)
    }

    override fun type(): FoliagePlacerType<*> = TreeUtilRegistry.LEAF_SPHEROID_FOLIAGE_PLACER

    override fun createFoliage(
        level: WorldGenLevel,
        foliageSetter: FoliageSetter,
        random: RandomSource,
        config: TreeConfiguration,
        treeHeight: Int,
        foliageAttatchment: FoliageAttachment,
        foliageHeight: Int,
        leafRadius: Int,
        offset: Int
    ) {
        val center = foliageAttatchment.pos().above(offset)

        placeSpheroid(
            level,
            foliageSetter,
            VALID_TREE_POS,
            random,
            center,
            foliageAttatchment.radiusOffset() + horizontalRadius + random.nextInt(randomHorizontal + 1),
            foliageAttatchment.radiusOffset() + verticalRadius + random.nextInt(randomVertical + 1),
            verticalBias,
            config.foliageProvider
        )

        if (shagFactor > 0) {
            repeat(shagFactor) {
                val randomYaw = random.nextFloat() * Mth.TWO_PI
                val randomPitch = random.nextFloat() * 2f - 1f
                val yUnit = Mth.sqrt(1 - randomPitch * randomPitch)
                val xCircleOffset = yUnit * Mth.cos(randomYaw.toDouble()) * (horizontalRadius - 1f)
                val zCircleOffset = yUnit * Mth.sin(randomYaw.toDouble()) * (horizontalRadius - 1f)

                val placement = center.offset(
                    (xCircleOffset + (xCircleOffset.toInt() shr 31)).toInt(),
                    (randomPitch * (verticalRadius + 0.25f) + verticalBias).toInt(),
                    (zCircleOffset + (zCircleOffset.toInt() shr 31)).toInt()
                )

                placeLeafCluster(level, foliageSetter, random, placement.immutable(), config.foliageProvider)
            }
        }
    }

    private fun placeLeafCluster(
        level: WorldGenLevel,
        foliageSetter: FoliageSetter,
        random: RandomSource,
        pos: BlockPos,
        state: BlockStateProvider
    ) {
        placeLeaf(level, foliageSetter, VALID_TREE_POS, pos, state, random)
        placeLeaf(level, foliageSetter, VALID_TREE_POS, pos.east(), state, random)
        placeLeaf(level, foliageSetter, VALID_TREE_POS, pos.south(), state, random)
        placeLeaf(level, foliageSetter, VALID_TREE_POS, pos.offset(1, 0, 1), state, random)
    }

    override fun foliageHeight(
        random: RandomSource,
        i: Int,
        baseTreeFeatureConfig: TreeConfiguration
    ): Int = 0

    override fun shouldSkipLocation(
        random: RandomSource,
        dx: Int,
        dy: Int,
        dz: Int,
        radius: Int,
        large: Boolean
    ): Boolean = false
}