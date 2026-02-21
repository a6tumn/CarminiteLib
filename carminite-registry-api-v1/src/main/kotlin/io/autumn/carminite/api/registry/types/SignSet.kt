package io.autumn.carminite.api.registry.types

import net.minecraft.world.item.SignItem
import net.minecraft.world.level.block.Block

/**
 * Data structure containg a sign block, its wall block, ahnd its item. Useful for
 * other functions in this library which streamline sign registration.
 *
 * Members can be referenced using dot notation
 **/

data class SignSet(val standing: Block, val wall: Block, val item: SignItem)
