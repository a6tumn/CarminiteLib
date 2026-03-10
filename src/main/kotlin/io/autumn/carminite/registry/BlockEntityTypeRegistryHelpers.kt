@file:Suppress("unused")

package io.autumn.carminite.registry

import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

fun <T : BlockEntity> registerGenericBlockEntityType(
    namespaceAndPath: Identifier,
    factory: (BlockPos, BlockState) -> T,
    vararg blocks: Block
): BlockEntityType<T> {
    return Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        namespaceAndPath,
        FabricBlockEntityTypeBuilder.create(factory, *blocks).build()
    )
}