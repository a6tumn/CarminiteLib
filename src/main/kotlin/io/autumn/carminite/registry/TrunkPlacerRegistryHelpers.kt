@file:Suppress("unused")

package io.autumn.carminite.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType

fun <P : TrunkPlacer> registerGenericTrunkPlacer(
    name: String,
    codec: MapCodec<P>
): TrunkPlacerType<P> {
    return Registry.register(
        BuiltInRegistries.TRUNK_PLACER_TYPE,
        name,
        TrunkPlacerType(codec)
    )
}