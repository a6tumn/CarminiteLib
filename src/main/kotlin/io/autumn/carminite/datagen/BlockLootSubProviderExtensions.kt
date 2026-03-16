@file:Suppress("unused")

package io.autumn.carminite.datagen

import io.autumn.carminite.wood.WoodSet
import net.minecraft.data.loot.BlockLootSubProvider

fun BlockLootSubProvider.createWoodSetDrops(woodSet: WoodSet) {
    dropSelf(woodSet.log)
    dropSelf(woodSet.strippedLog)
    dropSelf(woodSet.wood)
    dropSelf(woodSet.strippedWood)
    createLeavesDrops(woodSet.leaves, woodSet.sapling, 0.05f)
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
}