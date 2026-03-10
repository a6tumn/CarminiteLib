@file:Suppress("unused")

package io.autumn.carminite.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType

fun <P : FoliagePlacer> registerGenericFoliagePlacer(
    name: String,
    codec: MapCodec<P>
): FoliagePlacerType<P> {
    return Registry.register(
        BuiltInRegistries.FOLIAGE_PLACER_TYPE,
        name,
        FoliagePlacerType(codec)
    )
}