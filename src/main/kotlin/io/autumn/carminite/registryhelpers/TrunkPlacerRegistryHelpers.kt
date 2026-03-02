@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
/**
 * Collection of helper methods for streamlining trunk placer registration while
 * using and copying some logic from vanilla systems.
 *
 * These functions mirror private vanilla implementations. Defaults parameter values
 * are not provided in most cases as the arguments will need meaningful values for production code.
 **/

/**
 * Registers a generic trunk placer.
 *
 * Intended for generic trunk placers.
 *
 * @param name A string containing the name of the trunk placer (no default set).
 * @param codec The map codec associated with your trunk placer (no default set).
 **/
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