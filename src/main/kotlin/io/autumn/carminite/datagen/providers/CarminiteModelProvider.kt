@file:Suppress("unused")

package io.autumn.carminite.datagen.providers

import io.autumn.carminite.datagen.generators.CarminiteBlockModelGenerators
import io.autumn.carminite.datagen.generators.CarminiteItemModelGenerators
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators

abstract class CarminiteModelProvider(output: FabricPackOutput) : FabricModelProvider(output) {

    abstract fun generateCarminiteBlockStateModels(blockModelGenerators: CarminiteBlockModelGenerators)
    abstract fun generateCarminiteItemModels(itemModelGenerators: CarminiteItemModelGenerators)

    final override fun generateBlockStateModels(blockModelGenerators: BlockModelGenerators) {
        generateCarminiteBlockStateModels(
            CarminiteBlockModelGenerators(blockModelGenerators.blockStateOutput, blockModelGenerators.itemModelOutput, blockModelGenerators.modelOutput)
        )
    }

    final override fun generateItemModels(itemModelGenerators: ItemModelGenerators) {
        generateCarminiteItemModels(
            CarminiteItemModelGenerators(itemModelGenerators.itemModelOutput, itemModelGenerators.modelOutput)
        )
    }
}