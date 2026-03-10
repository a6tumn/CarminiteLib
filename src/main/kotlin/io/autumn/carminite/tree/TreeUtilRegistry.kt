@file:Suppress("unused")

package io.autumn.carminite.tree

import io.autumn.carminite.Carminite
import io.autumn.carminite.registry.registerGenericFoliagePlacer
import io.autumn.carminite.registry.registerGenericTreeDecorator
import io.autumn.carminite.registry.registerGenericTrunkPlacer
import io.autumn.carminite.tree.decorators.TreeRootsDecorator
import io.autumn.carminite.tree.foliageplacers.LeafSpheroidFoliagePlacer
import io.autumn.carminite.tree.trunkplacers.BranchingTrunkPlacer
import io.autumn.carminite.tree.trunkplacers.MegaTrunkPlacer
import io.autumn.carminite.tree.trunkplacers.TrunkRiser
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType

object TreeUtilRegistry {
    val LEAF_SPHEROID_FOLIAGE_PLACER: FoliagePlacerType<LeafSpheroidFoliagePlacer> =
        registerGenericFoliagePlacer("leaf_spheroid_foliage_placer", LeafSpheroidFoliagePlacer.CODEC)

    val TREE_ROOTS_DECORATOR: TreeDecoratorType<TreeRootsDecorator> =
        registerGenericTreeDecorator("tree_roots_decorator", TreeRootsDecorator.CODEC)

    val BRANCHING_TRUNK_PLACER: TrunkPlacerType<BranchingTrunkPlacer> =
        registerGenericTrunkPlacer("branching_trunk_placer", BranchingTrunkPlacer.CODEC)

    val MEGA_TRUNK_PLACER: TrunkPlacerType<MegaTrunkPlacer> =
        registerGenericTrunkPlacer("mega_trunk_placer", MegaTrunkPlacer.CODEC)

    val TRUNK_RISER: TrunkPlacerType<TrunkRiser> =
        registerGenericTrunkPlacer("trunk_riser", TrunkRiser.CODEC)

    fun initialize() {
        Carminite.LOGGER?.info("Registering foliage placers for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering tree decorators for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering trunk placers for ${Carminite.NAMESPACE}.")
    }
}