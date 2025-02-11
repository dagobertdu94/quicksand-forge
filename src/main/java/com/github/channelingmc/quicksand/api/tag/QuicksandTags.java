package com.github.channelingmc.quicksand.api.tag;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class QuicksandTags {
    
    public static final TagKey<Biome> HAS_QUICKSAND_LAKE =
        TagKey.create(Registries.BIOME, QuicksandAPI.loc("has_feature/quicksand_lake"));
    public static final TagKey<Biome> HAS_RED_QUICKSAND_LAKE =
        TagKey.create(Registries.BIOME, QuicksandAPI.loc("has_feature/red_quicksand_lake"));
    
    public static final TagKey<Block> QUICKSAND =
        TagKey.create(Registries.BLOCK, QuicksandAPI.loc("quicksand"));
    public static final TagKey<Block> QUICKSAND_CAULDRON =
        TagKey.create(Registries.BLOCK, QuicksandAPI.loc("quicksand_cauldron"));
    
    public static final TagKey<EntityType<?>> QUICKSAND_WALKABLE_MOBS =
        TagKey.create(Registries.ENTITY_TYPE, QuicksandAPI.loc("quicksand_walkable_mobs"));
    public static final TagKey<EntityType<?>> SURVIVES_IN_QUICKSAND =
        TagKey.create(Registries.ENTITY_TYPE, QuicksandAPI.loc("survives_in_quicksand"));
    
}
