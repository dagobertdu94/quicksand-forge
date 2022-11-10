package com.github.channelingmc.quicksand.api;

import com.github.channelingmc.quicksand.common.block.QuicksandBlock;
import com.github.channelingmc.quicksand.common.block.QuicksandCauldronBlock;
import com.github.channelingmc.quicksand.common.levelgen.feature.QuicksandLakeFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class QuicksandAPI {
    
    public static final String ID = "quicksand";
    
    public static final DamageSource QUICKSAND_DMGSRC = new DamageSource("quicksand").bypassArmor();
    
    public static final Map<Item, CauldronInteraction> QUICKSAND_INTERACTIONS =
        CauldronInteraction.newInteractionMap();
    public static final Map<Item, CauldronInteraction> RED_QUICKSAND_INTERACTIONS =
        CauldronInteraction.newInteractionMap();
    
    public static final RegistryObject<QuicksandBlock> QUICKSAND_BLOCK =
        RegistryObject.create(loc("quicksand"), ForgeRegistries.BLOCKS);
    public static final RegistryObject<QuicksandCauldronBlock> QUICKSAND_CAULDRON =
        RegistryObject.create(loc("quicksand_cauldron"), ForgeRegistries.BLOCKS);
    public static final RegistryObject<QuicksandBlock> RED_QUICKSAND_BLOCK =
        RegistryObject.create(loc("red_quicksand"), ForgeRegistries.BLOCKS);
    public static final RegistryObject<QuicksandCauldronBlock> RED_QUICKSAND_CAULDRON =
        RegistryObject.create(loc("red_quicksand_cauldron"), ForgeRegistries.BLOCKS);
    
    public static final RegistryObject<SolidBucketItem> QUICKSAND_BUCKET =
        RegistryObject.create(loc("quicksand_bucket"), ForgeRegistries.ITEMS);
    public static final RegistryObject<SolidBucketItem> RED_QUICKSAND_BUCKET =
        RegistryObject.create(loc("red_quicksand_bucket"), ForgeRegistries.ITEMS);
    
    public static final RegistryObject<SoundEvent> ZOMBIE_CONVERTS_TO_HUSK =
        RegistryObject.create(loc("entity.zombie.converted_to_husk"), ForgeRegistries.SOUND_EVENTS);
    public static final RegistryObject<SoundEvent> DROWNED_CONVERTS_TO_ZOMBIE =
        RegistryObject.create(loc("entity.drowned.converted_to_zombie"), ForgeRegistries.SOUND_EVENTS);
    
    public static final RegistryObject<QuicksandLakeFeature> QUICKSAND_LAKE =
        RegistryObject.create(loc("quicksand_lake"), ForgeRegistries.FEATURES);
    
    public static Holder<ConfiguredFeature<QuicksandLakeFeature.Configuration, ?>> QUICKSAND_LAKE_CF;
    public static Holder<ConfiguredFeature<QuicksandLakeFeature.Configuration, ?>> RED_QUICKSAND_LAKE_CF;
    public static Holder<PlacedFeature> QUICKSAND_LAKE_PF;
    public static Holder<PlacedFeature> RED_QUICKSAND_LAKE_PF;
    
    public static ResourceLocation loc(String id) {
        return new ResourceLocation(ID, id);
    }
    
}
