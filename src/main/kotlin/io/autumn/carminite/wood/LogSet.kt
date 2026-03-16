package io.autumn.carminite.wood

import io.autumn.carminite.registry.registerGenericBlock
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor

data class LogSet(
    val namespaceAndPath: Identifier,
    val topMapColor: MapColor,
    val sideMapColor: MapColor
    ) {
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

    init {
        registerStrippables(log, strippedLog)
        registerStrippables(wood, strippedWood)
    }

    private fun registerStrippables(normal: Block, stripped: Block) {
        StrippableBlockRegistry.register(normal, stripped)
    }
}
