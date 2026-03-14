@file:Suppress("unused")

package io.autumn.carminite

import io.autumn.carminite.tree.TreeUtilRegistry
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Carminite: ModInitializer {
    const val NAMESPACE = "carminite"
    val LOGGER: Logger? = LogManager.getLogger(NAMESPACE)

    override fun onInitialize() {
        TreeUtilRegistry.initialize()
    }

    fun namespaceAndPath(path: String): Identifier = Identifier.fromNamespaceAndPath(NAMESPACE, path)
}