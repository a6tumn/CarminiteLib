package io.autumn.carminite.types

import net.minecraft.world.item.HangingSignItem
import net.minecraft.world.level.block.Block

/**
 * Data structure containg a hanging sign block, its wall block, ahnd its item. Useful for
 * other functions in this library which streamline hanging sign registration.
 *
 * Members can be referenced using dot notation
 **/

data class HangingSignSet(val hanging: Block, val wallHanging: Block, val item: HangingSignItem)
