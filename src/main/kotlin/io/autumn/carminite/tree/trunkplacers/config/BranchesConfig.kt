package io.autumn.carminite.tree.trunkplacers.config

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

data class BranchesConfig(
    val branchProvider: BlockStateProvider,
    val branchCount: Int,
    val randomAddBranches: Int,
    val length: Double,
    val randomAddLength: Double,
    val spacingYaw: Double,
    val downwardsPitch: Double
) {
    companion object {
        val CODEC: Codec<BranchesConfig> = RecordCodecBuilder.create { instance ->
            instance.group(
                BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter { it.branchProvider },
                Codec.intRange(0, 16).fieldOf("count_minimum").forGetter { it.branchCount },
                Codec.intRange(0, 16).fieldOf("random_add_count").orElse(0).forGetter { it.randomAddBranches },
                Codec.doubleRange(1.0, 24.0).fieldOf("length").forGetter { it.length },
                Codec.doubleRange(0.0, 16.0).fieldOf("random_add_length").orElse(0.0).forGetter { it.randomAddLength },
                Codec.doubleRange(0.0, 0.5).fieldOf("spacing_yaw").orElse(0.3).forGetter { it.spacingYaw },
                Codec.doubleRange(0.0, 1.0).fieldOf("downwards_pitch").orElse(0.2).forGetter { it.downwardsPitch }
            ).apply(instance, ::BranchesConfig)
        }
    }
}
