@file:Suppress("unused")

package io.autumn.carminite.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType

/**
 * Collection of helper methods for streamlining tree decorator registration while
 * using and copying some logic from vanilla systems.
 *
 * These functions mirror private vanilla implementations. Defaults parameter values
 * are not provided in most cases as the arguments will need meaningful values for production code.
 **/

/**
 * Registers a generic tree decorators.
 *
 * Intended for generic tree decorators.
 *
 * @param name A string containing the name of the tree decorator (no default set).
 * @param codec The map codec associated with your tree decorator (no default set).
 **/
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