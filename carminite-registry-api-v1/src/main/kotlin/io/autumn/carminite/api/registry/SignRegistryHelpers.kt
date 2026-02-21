package io.autumn.carminite.api.registry

import io.autumn.carminite.api.registry.data.HangingSignSet
import io.autumn.carminite.api.registry.data.SignSet
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

/**
 * Collection of helper methods for streamlining sign block and item registration while
 * using and copying some logic from vanilla systems.
 *
 * These functions chain registrations to streamline registration for mods with lots of
 * repeating registration patterns. Defaults parameter values are not provided in most
 * cases as the arguments will need meaningfulvalues for production code.
 **/

/**
 * Registers a sign block, wall sign block, and sign item.
 *
 * Intended for sign blocks.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the wood type(no default set).
 * @param woodType The wood type that the sign belongs to (no default set).
 * @param settings The settings which you want your blocks to be registered with (no default set).
 **/
fun registerSign(
    namespaceAndPath: Identifier,
    woodType: WoodType,
    settings: BlockBehaviour.Properties
): SignSet {
    val standing = registerGenericBlock(
        namespaceAndPath.withSuffix("_sign"),
        { p -> StandingSignBlock(woodType, p) },
        settings,
        false
    )
    val wall = registerGenericBlock(
        namespaceAndPath.withSuffix("_wall_sign"),
        { p -> WallSignBlock(woodType, p) },
        settings.overrideLootTable(standing.lootTable).overrideDescription(standing.descriptionId),
        false
    )

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

/**
 * Registers a hanging sign block, wall hanging sign block, and hanging sign item.
 *
 * Intended for hanging sign blocks.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the wood type(no default set).
 * @param woodType The wood type that the hanging sign belongs to (no default set).
 * @param settings The settings which you want your blocks to be registered with (no default set).
 **/
fun registerHangingSign(
    namespaceAndPath: Identifier,
    woodType: WoodType,
    settings: BlockBehaviour.Properties
): HangingSignSet {
    val hanging = registerGenericBlock(
        namespaceAndPath.withSuffix("_hanging_sign"),
        { p -> CeilingHangingSignBlock(woodType, p) },
        settings,
        false
    )
    val wallHanging = registerGenericBlock(
        namespaceAndPath.withSuffix("_wall_hanging_sign"),
        { p -> WallHangingSignBlock(woodType, p) },
        settings.overrideLootTable(hanging.lootTable).overrideDescription(hanging.descriptionId),
        false
    )

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