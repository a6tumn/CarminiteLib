package io.autumn.carminite.tool

enum class ToolType(
    val idSuffix: String,
    val langSuffix: String
) {
    SWORD("_sword", "Sword"),
    SHOVEL("_shovel", "Shovel"),
    PICKAXE("_pickaxe", "Pickaxe"),
    AXE("_axe", "Axe"),
    HOE("_hoe", "Hoe"),
}