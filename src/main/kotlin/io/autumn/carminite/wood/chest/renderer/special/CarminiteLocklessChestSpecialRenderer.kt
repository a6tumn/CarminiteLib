@file:Suppress("unused")

package io.autumn.carminite.wood.chest.renderer.special

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.`object`.chest.ChestModel
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.client.resources.model.sprite.SpriteGetter
import net.minecraft.client.resources.model.sprite.SpriteId
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.state.properties.ChestType
import org.joml.Vector3fc
import java.util.function.Consumer

class CarminiteLocklessChestSpecialRenderer(private val sprites: SpriteGetter, private val model: ChestModel, private val sprite: SpriteId, private val openness: Float) : NoDataSpecialModelRenderer {

    override fun submit(poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector, lightCoords: Int, overlayCoords: Int, hasFoil: Boolean, outlineColor: Int) {
        submitNodeCollector.submitModel(
            model,
            openness,
            poseStack,
            sprite.renderType(RenderTypes::entitySolid),
            lightCoords,
            overlayCoords,
            -1,
            sprites[sprite],
            outlineColor,
            null
        )
    }

    override fun getExtents(output: Consumer<Vector3fc>) {
        val poseStack = PoseStack()
        model.setupAnim(openness)
        model.root().getExtentsForGui(poseStack, output)
    }

    data class Unbaked(val texture: Identifier, val openness: Float = 0f, val chestType: ChestType = ChestType.SINGLE) : NoDataSpecialModelRenderer.Unbaked {

        override fun type(): MapCodec<Unbaked> = MAP_CODEC

        override fun bake(context: SpecialModelRenderer.BakingContext): CarminiteLocklessChestSpecialRenderer {
            val model = ChestModel(context.entityModelSet().bakeLayer(getModel(chestType)))

            model.root().getChild("lock").visible = false

            val fullTexture = Sheets.CHEST_MAPPER.apply(texture)
            return CarminiteLocklessChestSpecialRenderer(context.sprites(), model, fullTexture, openness)
        }

        companion object {
            val MAP_CODEC: MapCodec<Unbaked> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture),
                    Codec.FLOAT.optionalFieldOf("openness", 0.0f).forGetter(Unbaked::openness),
                    ChestType.CODEC.optionalFieldOf("chest_type", ChestType.SINGLE).forGetter(Unbaked::chestType)
                ).apply(instance, ::Unbaked)
            }

            private fun getModel(type: ChestType): ModelLayerLocation =
                when (type) {
                    ChestType.SINGLE -> ModelLayers.CHEST
                    ChestType.RIGHT -> ModelLayers.DOUBLE_CHEST_RIGHT
                    ChestType.LEFT -> ModelLayers.DOUBLE_CHEST_LEFT
                }
        }
    }
}