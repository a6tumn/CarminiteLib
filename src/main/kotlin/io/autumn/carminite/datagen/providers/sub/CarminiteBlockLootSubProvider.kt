@file:Suppress("unused")

package io.autumn.carminite.datagen.providers.sub

import io.autumn.carminite.wood.WoodSet
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

abstract class CarminiteBlockLootSubProvider(
    explosionResistant: Set<Item>,
    enabledFeatures: FeatureFlagSet,
    registries: HolderLookup.Provider
) : BlockLootSubProvider(explosionResistant, enabledFeatures, registries) {
    val map: MutableMap<ResourceKey<LootTable>, LootTable.Builder> = mutableMapOf()

    fun createWoodSetDrops(woodSet: WoodSet, chestBlock: Block, trappedChestBlock: Block) {
        dropSelf(woodSet.log)
        dropSelf(woodSet.strippedLog)
        dropSelf(woodSet.wood)
        dropSelf(woodSet.strippedWood)
        createLeavesDrops(woodSet.leaves, woodSet.sapling, NORMAL_LEAVES_SAPLING_CHANCES[0])
        dropSelf(woodSet.sapling)
        dropSelf(woodSet.planks)
        dropSelf(woodSet.door)
        dropSelf(woodSet.trapdoor)
        dropSelf(woodSet.fence)
        dropSelf(woodSet.fenceGate)
        dropSelf(woodSet.stairs)
        dropSelf(woodSet.slab)
        dropSelf(woodSet.button)
        dropSelf(woodSet.pressurePlate)
        dropSelf(woodSet.standingSign)
        dropSelf(woodSet.wallSign)
        dropSelf(woodSet.hangingSign)
        dropSelf(woodSet.wallHangingSign)
        dropSelf(chestBlock)
        dropSelf(trappedChestBlock)
    }

    fun add(block: Block, builderFunction: (Block) -> LootTable.Builder) {
        add(block, builderFunction(block))
    }

    override fun add(block: Block, builder: LootTable.Builder) {
        val key = block.lootTable.orElseThrow {
            IllegalStateException("Block $block does not have loot table")
        }
        map[key] = builder
    }
}