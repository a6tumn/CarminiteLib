package io.autumn.carminite.types

import net.minecraft.world.level.block.Block

/**
 * Data structure containg a log block and its stripped variant. Useful for
 * other functions in this library which streamline log pair registration.
 *
 * Members can be referenced using dot notation
 **/

data class LogSet(val log: Block, val strippedLog: Block)
