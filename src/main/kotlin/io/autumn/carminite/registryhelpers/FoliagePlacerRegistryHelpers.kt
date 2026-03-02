@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType

/**
 * Collection of helper methods for streamlining foliage placer registration while
 * using and copying some logic from vanilla systems.
 *
 * These functions mirror private vanilla implementations. Defaults parameter values
 * are not provided in most cases as the arguments will need meaningful values for production code.
 **/

/**
 * Registers a generic foliage placer.
 *
 * Intended for generic foliage placers.
 *
 * @param name A string containing the name of the foliage placer (no default set).
 * @param codec The map codec associated with your foliage placer (no default set).
 **/
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