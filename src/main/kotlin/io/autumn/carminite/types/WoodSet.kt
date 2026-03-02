package io.autumn.carminite.types

import net.minecraft.world.level.block.Block

/**
 * Data structure containg a wood block and its stripped variant. Useful for
 * other functions in this library which streamline wood pair registration.
 *
 * Members can be referenced using dot notation
 **/

data class WoodSet(val wood: Block, val strippedWood: Block)