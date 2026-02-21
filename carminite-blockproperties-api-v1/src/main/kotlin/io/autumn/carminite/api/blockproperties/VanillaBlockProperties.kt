package io.autumn.carminite.api.blockproperties

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

/**
 * Collection of helper methods for creating vanilla-like
 * [BlockBehaviour.Properties] configurations.
 *
 * These functions mirror common property combinations used by vanilla
 * wooden blocks in Minecraft and are intended to reduce duplication
 * when registering Fabric blocks with consistent behavior. Sensible
 * defaults are provided as to help prevent crashes and speed up feature
 * testing, however, the arguments are intented to have meaningful
 * values in production code.
 **/
object VanillaBlockProperties {

    /**
     * Creates standard wood block properties.
     *
     * Intended for generic wood-based blocks including wood and
     * stripped wood.
     *
     * @param mapColor The color shown on maps (defaults to [MapColor.WOOD]).
     * @param soundType The sound type when walked on or broken (defaults to [SoundType.WOOD]).
     **/
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

    /**
     * Creates standard sapling block properties.
     *
     * Intended for generic sapling blocks.
     *
     * @param mapColor The color shown on maps (defaults to [MapColor.PLANT]).
     * @param soundType The sound type when walked on or broken (defaults to [SoundType.GRASS]).
     **/
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

    /**
     * Creates standard plank block properties.
     *
     * Intended for generic planks blocks.
     *
     * @param mapColor The color shown on maps (defaults to [MapColor.WOOD]).
     * @param strength Hardness and blast resistance (defaults to 2.0f & 3.0f).
     * @param soundType The sound type when walked on or broken (defaults to [SoundType.WOOD]).
     **/
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

    /**
     * Creates standard door block properties.
     *
     * Intended for generic door blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     * @param strength Hardness value (defaults to 3.0f).
     **/
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

    /**
     * Creates standard trapdoor block properties.
     *
     * Intended for generic trapdoor blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     * @param strength Hardness value (defaults to 3.0f).
     **/
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

    /**
     * Creates standard fence block properties.
     *
     * Intended for generic fence blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     * @param strength Hardness and blast resistance (defaults to 2.0f & 3.0f).
     **/
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

    /**
     * Creates standard fence gate block properties.
     *
     * Intended for generic fence gate blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     * @param strength Hardness and blast resistance (defaults to 2.0f & 3.0f).
     **/
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

    /**
     * Creates standard pressure plate block properties.
     *
     * Intended for generic pressure plate blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     **/
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

    /**
     * Creates standard shelf block properties.
     *
     * Intended for generic shelf blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     **/
    fun shelfProperties(
        baseBlock: Block = Blocks.OAK_PLANKS
    ): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of()
            .mapColor(baseBlock.defaultMapColor())
            .instrument(NoteBlockInstrument.BASS)
            .sound(SoundType.SHELF)
            .ignitedByLava()
            .strength(2.0f, 3.0f)

    /**
     * Creates standard sign block properties.
     *
     * Intended for generic sign blocks.
     *
     * @param baseBlock Block to inherit map color from (defaults to [Blocks.OAK_PLANKS]).
     */
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
}