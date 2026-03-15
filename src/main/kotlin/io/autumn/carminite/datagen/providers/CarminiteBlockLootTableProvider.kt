@file:Suppress("unused")

package io.autumn.carminite.datagen.providers

import io.autumn.carminite.datagen.providers.sub.CarminiteBlockLootSubProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableSubProvider
import net.fabricmc.fabric.impl.datagen.loot.FabricLootTableProviderImpl
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

abstract class CarminiteBlockLootTableProvider(
    private val output: FabricPackOutput,
    private val registriesFuture: CompletableFuture<HolderLookup.Provider>
) : CarminiteBlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), registriesFuture.join()), FabricLootTableSubProvider {
    private val excludedFromStrictValidation: MutableSet<Identifier> = mutableSetOf()
    abstract override fun generate()

    fun excludeFromStrictValidation(block: Block) {
        excludedFromStrictValidation.add(BuiltInRegistries.BLOCK.getKey(block))
    }

    override fun generate(biConsumer: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {
        generate()

        map.forEach { (resourceKey, builder) ->
            biConsumer.accept(resourceKey, builder)
        }

        if (output.isStrictValidationEnabled) {
            val missing = mutableSetOf<Identifier>()

            for (blockId in BuiltInRegistries.BLOCK.keySet()) {
                if (blockId.namespace == output.modId) {
                    val block = BuiltInRegistries.BLOCK.getValue(blockId)
                    val blockLootTableId = block.lootTable.orElse(null)
                    if (blockLootTableId != null && blockLootTableId.identifier().namespace == output.modId) {
                        if (!map.containsKey(blockLootTableId)) {
                            missing.add(blockId)
                        }
                    }
                }
            }

            missing.removeAll(excludedFromStrictValidation)

            if (missing.isNotEmpty()) {
                throw IllegalStateException("Missing loot table(s) for $missing")
            }
        }
    }

    override fun run(output: CachedOutput): CompletableFuture<*> {
        return FabricLootTableProviderImpl.run(output, this, LootContextParamSets.BLOCK, this.output, registriesFuture)
    }

    override fun getName(): String = "BlockLootTableProvider"
}