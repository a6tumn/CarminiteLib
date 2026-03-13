@file:Suppress("unused")

package io.autumn.carminite.tool

import io.autumn.carminite.registry.keyOfItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.level.block.Block

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

data class ToolSet(
    val nameSpaceAndPath: Identifier,
    val incorrectForTag: TagKey<Block>,
    val repairWithTag: TagKey<Item>,
    val durability: Int,
    val speed: Float,
    val attackDamageBonus: Float,
    val enchantmentValue: Int,
    val damageOffsetFromIron: Float,
    val speedOffsetFromIron: Float,
    val enabledTools: Set<ToolType> = ToolType.entries.toSet()
) {
    private val swordDamage = 3.0f + damageOffsetFromIron
    private val shovelDamage = 1.5f + damageOffsetFromIron
    private val pickaxeDamage = 1.0f + damageOffsetFromIron
    private val axeDamage = 6.0f + damageOffsetFromIron
    private val hoeDamage = -2.0f + damageOffsetFromIron

    private val swordSpeed = -2.4f + speedOffsetFromIron
    private val shovelSpeed = -3.0f + speedOffsetFromIron
    private val pickaxeSpeed = -2.8f + speedOffsetFromIron
    private val axeSpeed = -3.1f + speedOffsetFromIron
    private val hoeSpeed = -1.0f + speedOffsetFromIron

    val toolMaterial = ToolMaterial(incorrectForTag, durability, speed, attackDamageBonus, enchantmentValue, repairWithTag)

    val sword = registerIfEnabled(ToolType.SWORD) {
        Item.Properties().sword(toolMaterial, swordDamage, swordSpeed)
    }
    val shovel = registerIfEnabled(ToolType.SHOVEL) {
        Item.Properties().shovel(toolMaterial, shovelDamage, shovelSpeed)
    }
    val pickaxe = registerIfEnabled(ToolType.PICKAXE) {
        Item.Properties().pickaxe(toolMaterial, pickaxeDamage, pickaxeSpeed)
    }
    val axe = registerIfEnabled(ToolType.AXE) {
        Item.Properties().axe(toolMaterial, axeDamage, axeSpeed)
    }
    val hoe = registerIfEnabled(ToolType.HOE) {
        Item.Properties().hoe(toolMaterial, hoeDamage, hoeSpeed)
    }

    val setId = nameSpaceAndPath.path
    val setName = nameSpaceAndPath.path.toLangCase()

    val listOfTools = listOfNotNull(sword, shovel, pickaxe, axe, hoe)

    val mapOfToolsToTypes: Map<ToolType, Item?> = mapOf(
        ToolType.SWORD to sword,
        ToolType.SHOVEL to shovel,
        ToolType.PICKAXE to pickaxe,
        ToolType.AXE to axe,
        ToolType.HOE to hoe
    )
    val mapOfTypesToItemTags: Map<ToolType, TagKey<Item>> = mapOf(
        ToolType.SWORD to ItemTags.SWORDS,
        ToolType.SHOVEL to ItemTags.SHOVELS,
        ToolType.PICKAXE to ItemTags.PICKAXES,
        ToolType.AXE to ItemTags.AXES,
        ToolType.HOE to ItemTags.HOES
    )

    private fun registerIfEnabled(
        type: ToolType,
        settings: () -> Item.Properties
    ): Item? {
        return if (type !in enabledTools) null else {
            val id = nameSpaceAndPath.withSuffix(type.idSuffix)
            registerGenericItem(id, ::Item, settings())
        }
    }

    private fun <T : Item> registerGenericItem(
        namespaceAndPath: Identifier,
        itemFactory: (Item.Properties) -> T,
        settings: Item.Properties
    ): T {
        val itemKey = keyOfItem(namespaceAndPath)
        val item = itemFactory(settings.setId(itemKey))

        Registry.register(BuiltInRegistries.ITEM, itemKey, item)

        return item
    }

    private fun String.toLangCase(): String = this.split("_").joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
}
