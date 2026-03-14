@file:Suppress("unused")

package io.autumn.carminite.datagen.generators

import io.autumn.carminite.wood.WoodSet
import io.autumn.carminite.wood.chest.renderer.special.CarminiteLocklessChestSpecialRenderer
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelOutput
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.renderer.item.ItemModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import java.util.function.Consumer

class CarminiteBlockModelGenerators(
    blockStateOutput: Consumer<BlockModelDefinitionGenerator>,
    itemModelOutput: ItemModelOutput,
    modelOutput: BiConsumer<Identifier, ModelInstance>
) : BlockModelGenerators(blockStateOutput, itemModelOutput, modelOutput) {
    fun createWoodSetModels(woodSet: WoodSet, namespace: String, leavesTintColor: Int, chestBlock: Block, trappedChestBlock: Block, lockless: Boolean) {
        woodProvider(woodSet.log).logWithHorizontal(woodSet.log).wood(woodSet.wood)
        woodProvider(woodSet.strippedLog).logWithHorizontal(woodSet.strippedLog).wood(woodSet.strippedWood)
        createTintedLeaves(woodSet.leaves, TexturedModel.LEAVES, leavesTintColor)
        createPlantWithDefaultItem(woodSet.sapling, woodSet.pottedSapling, PlantType.NOT_TINTED)
        createHangingSign(woodSet.strippedLog, woodSet.hangingSign, woodSet.wallHangingSign)
        family(woodSet.planks).generateFor(woodSet.blockFamily)
        if(lockless) {
            createLocklessChest(chestBlock, woodSet.planks, Identifier.fromNamespaceAndPath(namespace,woodSet.woodId))
            createLocklessChest(trappedChestBlock, woodSet.planks, Identifier.fromNamespaceAndPath(namespace,"trapped_${woodSet.woodId}"))
        } else {
            createChest(chestBlock, woodSet.planks, Identifier.fromNamespaceAndPath(namespace, woodSet.woodId), false)
            createChest(trappedChestBlock, woodSet.planks, Identifier.fromNamespaceAndPath(namespace,"trapped_${woodSet.woodId}"), false)
        }
    }

    fun createPlantWithAltPotted(name: String, standAlone: Block, potted: Block, pottedTexure: Identifier, plantType: PlantType) {
        registerSimpleItemModel(
            standAlone.asItem(),
            plantType.createItemModel(this, standAlone)
        )
        createPlant(name, standAlone, potted, pottedTexure, plantType)
    }

    private fun createPlant(name: String, standAlone: Block, potted: Block, pottedTexure: Identifier, plantType: PlantType) {
        createCrossBlock(standAlone, plantType)

        val textures = plantType.getPlantTextureMapping(standAlone)

        val pottedTexture = Material(pottedTexure)

        textures.put(TextureSlot.PLANT, pottedTexture)

        val model = plainVariant(
            plantType.crossPot.create(potted, textures, modelOutput)
        )

        blockStateOutput.accept(createSimpleBlock(potted, model))
    }

     fun createLocklessChest(block: Block, particle: Block, texture: Identifier) {
        createParticleOnlyBlock(block, particle)

        val chestItem: Item = block.asItem()
        val itemModelBase: Identifier = ModelTemplates.CHEST_INVENTORY.create(chestItem, TextureMapping.particle(particle), modelOutput)
        val plainModel: ItemModel.Unbaked = ItemModelUtils.specialModel(itemModelBase, CarminiteLocklessChestSpecialRenderer.Unbaked(texture))

        itemModelOutput.accept(chestItem, plainModel)
    }
}