@file:Suppress("unused")

package io.autumn.carminite.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

fun <P : TreeDecorator> registerGenericTreeDecorator(
    name: String,
    codec: MapCodec<P>
): TreeDecoratorType<P> {
    return Registry.register(
        BuiltInRegistries.TREE_DECORATOR_TYPE,
        name,
        TreeDecoratorType(codec)
    )
}