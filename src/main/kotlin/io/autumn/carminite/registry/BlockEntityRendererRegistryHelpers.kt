@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

fun <T : BlockEntity, S : BlockEntityRenderState> registerGenericBlockEntityRenderer(
    type: BlockEntityType<T>,
    factory: (BlockEntityRendererProvider.Context) -> BlockEntityRenderer<T, S>
) {
    BlockEntityRenderers.register(type) { context ->
        factory(context)
    }
}