@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.core.Registry
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier

fun registerGenericParticle(
    namespaceAndPath: Identifier,
    particleType: SimpleParticleType
): SimpleParticleType =
        Registry.register(
            BuiltInRegistries.PARTICLE_TYPE,
            namespaceAndPath,
            particleType
        )
