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

/**
 * Collection of helper methods for streamlining block entity type registration while
 * using and copying some logic from vanilla systems.
 *
 * Defaults parameter values are not provided in most cases as the arguments will
 * need meaningful values for production code.
 **/

/**
 * Registers a block entity types.
 *
 * Intended for block entity types.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the block (no default set).
 * @param factory An instance of the block entity class providing blockpos and blockstate to the registration factory (no default set).
 * @param blocks The blocks which the block entity type will be registered to (no default set).
 **/
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