@file:Suppress("unused")

package io.autumn.carminite.tree.config

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import kotlin.math.max

class CarminiteTreeFeatureConfig(
    val trunkProvider: BlockStateProvider,
    val leavesProvider: BlockStateProvider,
    val branchProvider: BlockStateProvider,
    val rootsProvider: BlockStateProvider,
    val minHeight: Int,
    chanceFiveFirst: Int,
    chanceFiveSecond: Int,
    val hasLeaves: Boolean,
    val checkWater: Boolean,
    val decorators: List<TreeDecorator>
): FeatureConfiguration {
    val chanceAddFiveFirst: Int = max(chanceFiveFirst, 1)
    val chanceAddFiveSecond: Int = max(chanceFiveSecond, 1)

    @Transient
    var forcePlacement: Boolean = false

    fun forcePlacement() {
        forcePlacement = true
    }

    companion object {
        val CODEC: Codec<CarminiteTreeFeatureConfig> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    BlockStateProvider.CODEC.fieldOf("trunk_provider")
                        .forGetter { it.trunkProvider },
                    BlockStateProvider.CODEC.fieldOf("leaves_provider")
                        .forGetter { it.leavesProvider },
                    BlockStateProvider.CODEC.fieldOf("branch_provider")
                        .forGetter { it.branchProvider },
                    BlockStateProvider.CODEC.fieldOf("roots_provider")
                        .forGetter { it.rootsProvider },
                    Codec.INT.fieldOf("minimum_size").orElse(20)
                        .forGetter { it.minHeight },
                    Codec.INT.fieldOf("add_first_five_chance").orElse(1)
                        .forGetter { it.chanceAddFiveFirst },
                    Codec.INT.fieldOf("add_second_five_chance").orElse(1)
                        .forGetter { it.chanceAddFiveSecond },
                    Codec.BOOL.fieldOf("has_leaves").orElse(true)
                        .forGetter { it.hasLeaves },
                    Codec.BOOL.fieldOf("check_water").orElse(false)
                        .forGetter { it.checkWater },
                    TreeDecorator.CODEC.listOf().fieldOf("decorators")
                        .orElseGet { ImmutableList.of() }
                        .forGetter { it.decorators }
                ).apply(instance, ::CarminiteTreeFeatureConfig)
            }
    }

    class Builder(
        private val trunkProvider: BlockStateProvider,
        private val leavesProvider: BlockStateProvider,
        private val branchProvider: BlockStateProvider,
        private val rootsProvider: BlockStateProvider
    ) {
        private var baseHeight = 0
        private var chanceFirstFive = 0
        private var chanceSecondFive = 0
        private var hasLeaves = true
        private var checkWater = false
        private val decorators: MutableList<TreeDecorator> = Lists.newArrayList()

        fun minHeight(height: Int) = apply {
            baseHeight = height
        }

        fun chanceFirstFive(chance: Int) = apply {
            chanceFirstFive = chance
        }

        fun chanceSecondFive(chance: Int) = apply {
            chanceSecondFive = chance
        }

        fun noLeaves() = apply {
            hasLeaves = false
        }

        fun checksWater() = apply {
            checkWater = true
        }

        fun addDecorator(deco: TreeDecorator) = apply {
            decorators.add(deco)
        }

        fun build(): CarminiteTreeFeatureConfig {
            return CarminiteTreeFeatureConfig(
                trunkProvider,
                leavesProvider,
                branchProvider,
                rootsProvider,
                baseHeight,
                chanceFirstFive,
                chanceSecondFive,
                hasLeaves,
                checkWater,
                decorators
            )
        }
    }
}