@file:Suppress("unused")

package io.autumn.carminite.wood.chest.renderer.special

import io.autumn.carminite.Carminite
import net.minecraft.client.renderer.special.SpecialModelRenderers

object CarminiteSpecialRenderers {
    fun initialize() {
        Carminite.LOGGER?.info("Registering special renderers for ${Carminite.NAMESPACE}.")
        SpecialModelRenderers.ID_MAPPER.put(Carminite.namespaceAndPath("lockless_chest"), CarminiteLocklessChestSpecialRenderer.Unbaked.MAP_CODEC)
    }
}