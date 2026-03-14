@file:Suppress("unused")

package io.autumn.carminite

import io.autumn.carminite.wood.chest.renderer.special.CarminiteSpecialRenderers
import net.fabricmc.api.ClientModInitializer

object CarminiteClient: ClientModInitializer {
    override fun onInitializeClient() {
        CarminiteSpecialRenderers.initialize()
    }
}