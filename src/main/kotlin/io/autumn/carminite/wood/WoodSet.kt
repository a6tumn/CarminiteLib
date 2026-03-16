@file:Suppress("unused")

package io.autumn.carminite.wood

import io.autumn.carminite.property.*
import io.autumn.carminite.registry.registerGenericBlock
import io.autumn.carminite.registry.registerStair
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.grower.TreeGrower
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.WoodType
import net.minecraft.world.level.material.MapColor

data class WoodSet(
    val namespaceAndPath: Identifier,
    val blockSetType: BlockSetType,
    val woodType: WoodType,
    val treeGrower: TreeGrower,
    val topMapColor: MapColor,
    val sideMapColor: MapColor
) {
    val woodPath = namespaceAndPath.path
    val woodName = namespaceAndPath.path.toLangCase()

    private var logSet = LogSet(namespaceAndPath, topMapColor, sideMapColor,)
    val log = logSet.log
    val strippedLog = logSet.strippedLog
    val wood = logSet.wood
    val strippedWood = logSet.strippedWood

    val planks = registerGenericBlock(
        namespaceAndPath.withSuffix("_planks"),
        ::Block,
        plankProperties(sideMapColor)
    )

    private var signSet = SignSet(namespaceAndPath, woodType, planks)
    val standingSign = signSet.standingSign
    val wallSign = signSet.wallSign
    val signItem = signSet.signItem
    val hangingSign = signSet.hangingSign
    val wallHangingSign = signSet.wallHangingSign
    val hangingSignItem = signSet.hangingSignItem

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

    private fun String.toLangCase(): String = this.split("_").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}