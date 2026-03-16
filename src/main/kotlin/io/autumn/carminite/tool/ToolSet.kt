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

data class ToolSet(
    val nameSpaceAndPath: Identifier,
    val toolMaterial: ToolMaterial,
    val enabledTools: Set<ToolType> = ToolType.entries.toSet(),
    val speedPerTool: Map<ToolType, Float>,
    val damagePerTool: Map<ToolType, Float>
) {
    val setId: String = nameSpaceAndPath.path
    val setName: String = setId.toLangCase()

    val tools: Map<ToolType, Item> = enabledTools.associateWith { type ->
        val id = nameSpaceAndPath.withSuffix(type.idSuffix)
        val properties = when (type) {
            ToolType.SWORD -> Item.Properties().sword(toolMaterial, damagePerTool[type]!!, speedPerTool[type]!!)
            ToolType.SHOVEL -> Item.Properties().shovel(toolMaterial, damagePerTool[type]!!, speedPerTool[type]!!)
            ToolType.PICKAXE -> Item.Properties().pickaxe(toolMaterial, damagePerTool[type]!!, speedPerTool[type]!!)
            ToolType.AXE -> Item.Properties().axe(toolMaterial, damagePerTool[type]!!, speedPerTool[type]!!)
            ToolType.HOE -> Item.Properties().hoe(toolMaterial, damagePerTool[type]!!, speedPerTool[type]!!)
        }
        registerGenericItem(id, ::Item, properties)
    }

    val listOfTools = tools.values.toList()

    val mapOfToolsToTypes: Map<ToolType, Item?> = ToolType.entries.associateWith { type ->
        tools[type]
    }

    val mapOfTypesToItemTags: Map<ToolType, TagKey<Item>> = mapOf(
        ToolType.SWORD to ItemTags.SWORDS,
        ToolType.SHOVEL to ItemTags.SHOVELS,
        ToolType.PICKAXE to ItemTags.PICKAXES,
        ToolType.AXE to ItemTags.AXES,
        ToolType.HOE to ItemTags.HOES
    )

    init {
        require(speedPerTool.keys.containsAll(enabledTools)) {
            "Speed must be provided for all enabled tools: missing ${enabledTools - speedPerTool.keys}"
        }
        require(damagePerTool.keys.containsAll(enabledTools)) {
            "Damage must be provided for all enabled tools: missing ${enabledTools - damagePerTool.keys}"
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

    private fun String.toLangCase(): String = this.split("_").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
