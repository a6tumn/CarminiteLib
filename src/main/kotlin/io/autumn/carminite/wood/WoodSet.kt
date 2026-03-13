@file:Suppress("unused")

package io.autumn.carminite.wood

import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor
import io.autumn.carminite.property.*
import io.autumn.carminite.registry.registerGenericBlock
import io.autumn.carminite.registry.registerStair
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.WoodTypeBuilder
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.BlockFamily
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.SignItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ButtonBlock
import net.minecraft.world.level.block.CeilingHangingSignBlock
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.FenceGateBlock
import net.minecraft.world.level.block.FlowerPotBlock
import net.minecraft.world.level.block.PressurePlateBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.TintedParticleLeavesBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.WallHangingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.entity.BlockEntityType

data class WoodSet(
    val namespaceAndPath: Identifier,
    val copyBlockSetType: BlockSetType,
    val copyWoodType: WoodType,
    val treeGrower: TreeGrower,
    val topMapColor: MapColor,
    val sideMapColor: MapColor
) {
    val blockSetType = createBlockSetType(copyBlockSetType)
    val woodType = createWoodType(copyWoodType)

    val log = registerGenericBlock(
        namespaceAndPath.withSuffix("_log"),
        ::RotatedPillarBlock,
        Blocks.logProperties(topMapColor, sideMapColor, SoundType.WOOD),
        true
    )

    val strippedLog = registerGenericBlock(
        namespaceAndPath.withPrefix("stripped_").withSuffix("_log"),
        ::RotatedPillarBlock,
        Blocks.logProperties(topMapColor, topMapColor, SoundType.WOOD),
        true
    )

    val wood = registerGenericBlock(
        namespaceAndPath.withSuffix("_wood"),
        ::RotatedPillarBlock,
        Blocks.logProperties(sideMapColor, sideMapColor, SoundType.WOOD),
        true
    )

    val strippedWood = registerGenericBlock(
        namespaceAndPath.withPrefix("stripped_").withSuffix("_wood"),
        ::RotatedPillarBlock,
        Blocks.logProperties(topMapColor, topMapColor, SoundType.WOOD),
        true
    )

    val planks = registerGenericBlock(
        namespaceAndPath.withSuffix("_planks"),
        ::Block,
        plankProperties(sideMapColor)
    )

    val standingSign = registerGenericBlock(
        namespaceAndPath.withSuffix("_sign"),
        { p -> StandingSignBlock(woodType, p) },
        signProperties(planks),
        false
    )
    val wallSign = registerGenericBlock(
        namespaceAndPath.withSuffix("_wall_sign"),
        { p -> WallSignBlock(woodType, p) },
        signProperties(planks).overrideLootTable(standingSign.lootTable).overrideDescription(standingSign.descriptionId),
        false
    )

    val signItem = registerSignItem()

    val hangingSign = registerGenericBlock(
        namespaceAndPath.withSuffix("_hanging_sign"),
        { p -> CeilingHangingSignBlock(woodType, p) },
        signProperties(planks),
        false
    )
    val wallHangingSign = registerGenericBlock(
        namespaceAndPath.withSuffix("_wall_hanging_sign"),
        { p -> WallHangingSignBlock(woodType, p) },
        signProperties(planks).overrideLootTable(hangingSign.lootTable).overrideDescription(hangingSign.descriptionId),
        false
    )

    val hangingSignItem = registerHangingSignItem()

    val leaves = registerGenericBlock(
        namespaceAndPath.withSuffix("_leaves"),
        { p -> TintedParticleLeavesBlock(0.01f, p) },
        Blocks.leavesProperties(SoundType.GRASS)
    )

    val sapling = registerGenericBlock(
        namespaceAndPath.withSuffix("_sapling"),
        { p -> SaplingBlock(treeGrower, p) },
        saplingProperties()
    )

    val pottedSapling = registerGenericBlock(
        namespaceAndPath.withPrefix("potted_").withSuffix("_sapling"),
        { p -> FlowerPotBlock(sapling, p) },
        Blocks.flowerPotProperties())

    val stairs = registerStair(
        namespaceAndPath.withSuffix("_stairs"),
        planks
    )

    val slab = registerGenericBlock(
        namespaceAndPath.withSuffix("_slab"),
        ::SlabBlock,
        plankProperties(sideMapColor)
    )

    val door = registerGenericBlock(
        namespaceAndPath.withSuffix("_door"),
        { p -> DoorBlock(blockSetType, p) },
        doorProperties(planks)
    )

    val trapdoor = registerGenericBlock(
        namespaceAndPath.withSuffix("_trapdoor"),
        { p -> TrapDoorBlock(blockSetType, p) },
        trapdoorProperties(planks)
    )

    val fence = registerGenericBlock(
        namespaceAndPath.withSuffix("_fence"),
        ::FenceBlock,
        fenceProperties(planks)
    )

    val fenceGate = registerGenericBlock(
        namespaceAndPath.withSuffix("_fence_gate"),
        { p -> FenceGateBlock(woodType, p) },
        fenceGateProperties(planks)
    )

    val button = registerGenericBlock(
        namespaceAndPath.withSuffix("_button"),
        { p -> ButtonBlock(blockSetType, 30, p) },
        Blocks.buttonProperties())

    val pressurePlate = registerGenericBlock(
        namespaceAndPath.withSuffix("_pressure_plate"),
        { p -> PressurePlateBlock(blockSetType, p) },
        pressurePlateProperties(planks))

    val blockFamily = createBlockFamily()

    val woodId = namespaceAndPath.path
    val woodName = namespaceAndPath.path.toLangCase()

    init {
        registerStrippables(log, strippedLog)
        registerStrippables(wood, strippedWood)
        addSupportedSigns(standingSign, wallSign)
        addSupportedHangingSigns(hangingSign, wallHangingSign)
    }

    private fun registerStrippables(normal: Block, stripped: Block) {
        StrippableBlockRegistry.register(normal, stripped)
    }

    private fun registerSignItem(): Item {
        val itemKey = ResourceKey.create(Registries.ITEM, namespaceAndPath.withSuffix("_sign"))
        return Registry.register(
            BuiltInRegistries.ITEM,
            itemKey,
            SignItem(standingSign, wallSign, Item.Properties().stacksTo(16).setId(itemKey)))
    }

    private fun registerHangingSignItem(): Item {
        val itemKey = ResourceKey.create(Registries.ITEM, namespaceAndPath.withSuffix("_hanging_sign"))
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

    private fun createBlockSetType(copyType: BlockSetType): BlockSetType {
        return BlockSetTypeBuilder.copyOf(copyType).register(namespaceAndPath)
    }

    private fun createWoodType(copyType: WoodType): WoodType {
        return WoodTypeBuilder.copyOf(copyType).register(namespaceAndPath, blockSetType)
    }

    private fun createBlockFamily(): BlockFamily {
        return BlockFamily.Builder(planks)
            .door(door)
            .trapdoor(trapdoor)
            .fence(fence)
            .fenceGate(fenceGate)
            .stairs(stairs)
            .slab(slab)
            .button(button)
            .pressurePlate(pressurePlate)
            .sign(standingSign, wallSign)
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .family
    }

    private fun String.toLangCase(): String = this.split("_").joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
}