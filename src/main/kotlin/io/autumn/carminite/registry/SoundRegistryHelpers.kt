@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent

fun registerGenericSoundEvent(
    namespaceAndPath: Identifier
): SoundEvent =
    Registry.register(
        BuiltInRegistries.SOUND_EVENT,
        namespaceAndPath,
        SoundEvent.createVariableRangeEvent(namespaceAndPath)
    )