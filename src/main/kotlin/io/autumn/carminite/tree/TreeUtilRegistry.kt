@file:Suppress("unused")

package io.autumn.carminite.tree

import io.autumn.carminite.Carminite
import io.autumn.carminite.registry.registerGenericFoliagePlacer
import io.autumn.carminite.registry.registerGenericTreeDecorator
import io.autumn.carminite.registry.registerGenericTrunkPlacer
import io.autumn.carminite.tree.decorator.BlockInsertionDecorator
import io.autumn.carminite.tree.decorator.TreeRootsDecorator
import io.autumn.carminite.tree.decorator.TrunkSideDecorator
import io.autumn.carminite.tree.foliageplacer.LeafSpheroidFoliagePlacer
import io.autumn.carminite.tree.trunkplacer.BranchingTrunkPlacer
import io.autumn.carminite.tree.trunkplacer.TrunkRiser
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType

object TreeUtilRegistry {
    val LEAF_SPHEROID_FOLIAGE_PLACER: FoliagePlacerType<LeafSpheroidFoliagePlacer> =
        registerGenericFoliagePlacer("leaf_spheroid_foliage_placer", LeafSpheroidFoliagePlacer.CODEC)

    val TREE_ROOTS_DECORATOR: TreeDecoratorType<TreeRootsDecorator> =
        registerGenericTreeDecorator("tree_roots_decorator", TreeRootsDecorator.CODEC)

    val BLOCK_INSERTION_DECORATOR: TreeDecoratorType<BlockInsertionDecorator> =
        registerGenericTreeDecorator("block_insertion_decorator", BlockInsertionDecorator.CODEC)

    val TRUNK_SIDE_DECORATOR: TreeDecoratorType<TrunkSideDecorator> =
        registerGenericTreeDecorator("trunk_side_decorator", TrunkSideDecorator.CODEC)

    val BRANCHING_TRUNK_PLACER: TrunkPlacerType<BranchingTrunkPlacer> =
        registerGenericTrunkPlacer("branching_trunk_placer", BranchingTrunkPlacer.CODEC)

    val TRUNK_RISER: TrunkPlacerType<TrunkRiser> =
        registerGenericTrunkPlacer("trunk_riser", TrunkRiser.CODEC)

    fun initialize() {
        Carminite.LOGGER?.info("Registering foliage placers for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering tree decorators for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering trunk placers for ${Carminite.NAMESPACE}.")
    }
}