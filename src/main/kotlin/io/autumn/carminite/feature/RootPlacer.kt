@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

class RootPlacer(
    val placer: BiConsumer<BlockPos, BlockState>,
    val rootPenetrability: Int
)