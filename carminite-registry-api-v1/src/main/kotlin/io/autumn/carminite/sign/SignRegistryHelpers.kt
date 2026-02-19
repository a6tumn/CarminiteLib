package io.autumn.carminite.sign

import io.autumn.carminite.block.*
import io.autumn.carminite.sign.data.HangingSignSet
import io.autumn.carminite.sign.data.SignSet
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.HangingSignItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.SignItem
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.WoodType

fun registerSign(
    namespaceAndPath: Identifier,
    woodType: WoodType,
    settings: BlockBehaviour.Properties
): SignSet {
    val standing = registerGenericBlock(namespaceAndPath.withSuffix("_sign"), { p -> StandingSignBlock(woodType, p) }, settings, false)
    val wall = registerGenericBlock(namespaceAndPath.withSuffix("_wall_sign"), { p -> WallSignBlock(woodType, p) }, settings.overrideLootTable(standing.lootTable).overrideDescription(standing.descriptionId), false)

    val itemKey = ResourceKey.create(Registries.ITEM, namespaceAndPath.withSuffix("_sign"))
    val properties = Item.Properties()
        .stacksTo(16)
        .setId(itemKey)
    val signItem = SignItem(standing, wall, properties)

    Registry.register(BuiltInRegistries.ITEM, itemKey, signItem)

    BlockEntityType.SIGN.addValidBlock(standing)
    BlockEntityType.SIGN.addValidBlock(wall)

    return SignSet(standing, wall, signItem)
}

fun registerHangingSign(
    namespaceAndPath: Identifier,
    woodType: WoodType,
    settings: BlockBehaviour.Properties
): HangingSignSet {
    val hanging = registerGenericBlock(namespaceAndPath.withSuffix("_hanging_sign"), { p -> CeilingHangingSignBlock(woodType, p) }, settings, false)
    val wallHanging = registerGenericBlock(namespaceAndPath.withSuffix("_wall_hanging_sign"), { p -> WallHangingSignBlock(woodType, p) }, settings.overrideLootTable(hanging.lootTable).overrideDescription(hanging.descriptionId), false)

    val itemKey = ResourceKey.create(Registries.ITEM, namespaceAndPath.withSuffix("_hanging_sign"))
    val properties = Item.Properties()
        .stacksTo(16)
        .setId(itemKey)
    val signItem = HangingSignItem(hanging, wallHanging, properties)

    Registry.register(BuiltInRegistries.ITEM, itemKey, signItem)

    BlockEntityType.HANGING_SIGN.addValidBlock(hanging)
    BlockEntityType.HANGING_SIGN.addValidBlock(wallHanging)

    return HangingSignSet(hanging, wallHanging, signItem)
}