@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * Collection of helper methods for streamlining block entity renderer registration while
 * using and copying some logic from vanilla systems.
 *
 * Defaults parameter values are not provided in most cases as the arguments will
 * need meaningful values for production code.
 **/

/**
 * Registers a block entity renderer.
 *
 * Intended for block entity renderers.
 *
 * @param type The block entity type associated with the renderer (no default set).
 * @param factory An instance of the renderer class providing context to the registration factory (no default set).
 **/
fun <T : BlockEntity, S : BlockEntityRenderState> registerGenericBlockEntityRenderer(
    type: BlockEntityType<T>,
    factory: (BlockEntityRendererProvider.Context) -> BlockEntityRenderer<T, S>
) {
    BlockEntityRenderers.register(type) { context ->
        factory(context)
    }
}