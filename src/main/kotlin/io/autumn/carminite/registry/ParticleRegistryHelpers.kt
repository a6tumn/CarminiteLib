@file:Suppress("unused")

package io.autumn.carminite.registry

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
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

fun <T : ParticleOptions> registerGenericParticleProvider(
    particleType: ParticleType<T>,
    pendingParticleProvider: ParticleProviderRegistry.PendingParticleProvider<T>
) {
    ParticleProviderRegistry.getInstance().register(particleType, pendingParticleProvider)
}

