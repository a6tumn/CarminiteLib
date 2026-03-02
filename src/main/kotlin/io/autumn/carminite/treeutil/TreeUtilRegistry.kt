package io.autumn.carminite.treeutil

import io.autumn.carminite.Carminite
import io.autumn.carminite.registryhelpers.registerGenericFoliagePlacer
import io.autumn.carminite.registryhelpers.registerGenericTreeDecorator
import io.autumn.carminite.registryhelpers.registerGenericTrunkPlacer
import io.autumn.carminite.treeutil.decorators.TreeRootsDecorator
import io.autumn.carminite.treeutil.foliageplacers.LeafSpheroidFoliagePlacer
import io.autumn.carminite.treeutil.trunkplacers.BranchingTrunkPlacer
import io.autumn.carminite.treeutil.trunkplacers.TrunkRiser
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

    val TRUNK_RISER: TrunkPlacerType<TrunkRiser> =
        registerGenericTrunkPlacer("trunk_riser", TrunkRiser.CODEC)

    fun initialize() {
        Carminite.LOGGER?.info("Registering foliage placers for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering tree decorators for ${Carminite.NAMESPACE}.")
        Carminite.LOGGER?.info("Registering trunk placers for ${Carminite.NAMESPACE}.")
    }
}