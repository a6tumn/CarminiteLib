package io.autumn.carminite.wood

import io.autumn.carminite.property.signProperties
import io.autumn.carminite.registry.registerGenericBlock
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.SignItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.properties.WoodType

data class SignSet(
    val namespaceAndPath: Identifier,
    val woodType: WoodType,
    val planksBlock: Block
) {
    private val namespace = namespaceAndPath.namespace
    private val woodName = namespaceAndPath.path

    val standingSign = registerGenericBlock(
        Identifier.fromNamespaceAndPath(namespace, "${woodName}_sign"),
        { p -> StandingSignBlock(woodType, p) },
        signProperties(planksBlock),
        false
    )
    val wallSign = registerGenericBlock(
        Identifier.fromNamespaceAndPath(namespace, "${woodName}_wall_sign"),
        { p -> WallSignBlock(woodType, p) },
        signProperties(planksBlock).overrideLootTable(standingSign.lootTable).overrideDescription(standingSign.descriptionId),
        false
    )

    val signItem = registerSignItem()

    val hangingSign = registerGenericBlock(
        Identifier.fromNamespaceAndPath(namespace, "${woodName}_hanging_sign"),
        { p -> CeilingHangingSignBlock(woodType, p) },
        signProperties(planksBlock),
        false
    )
    val wallHangingSign = registerGenericBlock(
        Identifier.fromNamespaceAndPath(namespace, "${woodName}_wall_hanging_sign"),
        { p -> WallHangingSignBlock(woodType, p) },
        signProperties(planksBlock).overrideLootTable(hangingSign.lootTable).overrideDescription(hangingSign.descriptionId),
        false
    )

    val hangingSignItem = registerHangingSignItem()

    init {
        addSupportedSigns(standingSign, wallSign)
        addSupportedHangingSigns(hangingSign, wallHangingSign)
    }

    private fun registerSignItem(): Item {
        val itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, "${woodName}_sign"))
        return Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            SignItem(standingSign, wallSign, Item.Properties().stacksTo(16).setId(itemKey)))
    }

    private fun registerHangingSignItem(): Item {
        val itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, "${woodName}_hanging_sign"))
        return Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            SignItem(hangingSign, wallHangingSign, Item.Properties().stacksTo(16).setId(itemKey)))
    }

    private fun addSupportedSigns(standing: Block, wall: Block) {
        BlockEntityType.SIGN.addValidBlock(standing)
        BlockEntityType.SIGN.addValidBlock(wall)
    }

    private fun addSupportedHangingSigns(hanging: Block, wallHanging: Block) {
        BlockEntityType.HANGING_SIGN.addValidBlock(hanging)
        BlockEntityType.HANGING_SIGN.addValidBlock(wallHanging)
    }
}
