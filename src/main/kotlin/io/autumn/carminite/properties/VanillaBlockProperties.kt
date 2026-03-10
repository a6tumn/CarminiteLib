@file:Suppress("unused")

package io.autumn.carminite.properties

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

fun woodProperties(
    mapColor: MapColor = MapColor.WOOD,
    soundType: SoundType = SoundType.WOOD
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(mapColor)
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0f)
        .sound(soundType)
        .ignitedByLava()

fun saplingProperties(
    mapColor: MapColor = MapColor.PLANT,
    soundType: SoundType = SoundType.GRASS
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(mapColor)
        .noCollision()
        .randomTicks()
        .instabreak()
        .sound(soundType)
        .pushReaction(PushReaction.DESTROY)

fun plankProperties(
    mapColor: MapColor = MapColor.WOOD,
    strength: Pair<Float, Float> = 2.0f to 3.0f,
    soundType: SoundType = SoundType.WOOD
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(mapColor)
        .instrument(NoteBlockInstrument.BASS)
        .strength(strength.first, strength.second)
        .sound(soundType)
        .ignitedByLava()

fun doorProperties(
    baseBlock: Block = Blocks.OAK_PLANKS,
    strength: Float = 3.0f
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .instrument(NoteBlockInstrument.BASS)
        .strength(strength)
        .noOcclusion().ignitedByLava()
        .pushReaction(PushReaction.DESTROY)

fun trapdoorProperties(
    baseBlock: Block = Blocks.OAK_PLANKS,
    strength: Float = 3.0f
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .instrument(NoteBlockInstrument.BASS)
        .strength(strength)
        .noOcclusion()
        .ignitedByLava()
        .isValidSpawn(Blocks::never)

fun fenceProperties(
    baseBlock: Block = Blocks.OAK_PLANKS,
    strength: Pair<Float, Float> = 2.0f to 3.0f
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .strength(strength.first, strength.second)
        .sound(SoundType.WOOD)
        .ignitedByLava()

fun fenceGateProperties(
    baseBlock: Block = Blocks.OAK_PLANKS,
    strength: Pair<Float, Float> = 2.0f to 3.0f
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .strength(strength.first, strength.second)
        .ignitedByLava()

fun pressurePlateProperties(
    baseBlock: Block = Blocks.OAK_PLANKS
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .noCollision()
        .strength(0.5f)
        .ignitedByLava()
        .pushReaction(PushReaction.DESTROY)

fun shelfProperties(
    baseBlock: Block = Blocks.OAK_PLANKS
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .instrument(NoteBlockInstrument.BASS)
        .sound(SoundType.SHELF)
        .ignitedByLava()
        .strength(2.0f, 3.0f)

fun signProperties(
    baseBlock: Block = Blocks.OAK_PLANKS
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(baseBlock.defaultMapColor())
        .forceSolidOn()
        .instrument(NoteBlockInstrument.BASS)
        .noCollision()
        .strength(1.0f)
        .ignitedByLava()

fun carpetProperties(
    mapColor: MapColor = MapColor.WOOL
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(mapColor)
        .strength(0.1f)
        .sound(SoundType.WOOL)
        .ignitedByLava()

fun chestProperties(
    woodBlock: Block = Blocks.OAK_WOOD
): BlockBehaviour.Properties =
    BlockBehaviour.Properties.of()
        .mapColor(woodBlock.defaultMapColor())
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.5f)
        .sound(SoundType.WOOD).ignitedByLava()