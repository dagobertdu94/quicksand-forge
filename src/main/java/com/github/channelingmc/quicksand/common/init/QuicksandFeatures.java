package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.levelgen.feature.QuicksandLakeFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandFeatures {
    
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.FEATURES, QuicksandAPI.QUICKSAND_LAKE.getId(), QuicksandLakeFeature::new);
        /*
        if (event.getRegistryKey() == ForgeRegistries.Keys.FEATURES) {
                QuicksandAPI.QUICKSAND_LAKE_CF = registerConfiguredFeature("quicksand_lake",
                    QuicksandAPI.QUICKSAND_LAKE.get(),
                    new QuicksandLakeFeature.Configuration(
                        BlockStateProvider.simple(QuicksandBlocks.QUICKSAND.get().defaultBlockState()),
                        BlockStateProvider.simple(Blocks.SAND.defaultBlockState())
                    ));
                QuicksandAPI.RED_QUICKSAND_LAKE_CF = registerConfiguredFeature("red_quicksand_lake",
                    QuicksandAPI.QUICKSAND_LAKE.get(),
                    new QuicksandLakeFeature.Configuration(
                        BlockStateProvider.simple(QuicksandBlocks.RED_QUICKSAND.get().defaultBlockState()),
                        BlockStateProvider.simple(Blocks.RED_SAND.defaultBlockState())
                    ));
                QuicksandAPI.QUICKSAND_LAKE_PF = registerPlacedFeature("quicksand_lake",
                    QuicksandAPI.QUICKSAND_LAKE_CF,
                    RarityFilter.onAverageOnceEvery(QuicksandConfigs.COMMON.quicksandLakeRarity.get()),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                    BiomeFilter.biome()
                );
                QuicksandAPI.RED_QUICKSAND_LAKE_PF = registerPlacedFeature("red_quicksand_lake",
                    QuicksandAPI.RED_QUICKSAND_LAKE_CF,
                    RarityFilter.onAverageOnceEvery(QuicksandConfigs.COMMON.redQuicksandLakeRarity.get()),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                    BiomeFilter.biome()
                );
            }*/
    }
    
    @SubscribeEvent
    public static void registerGeneration(FMLCommonSetupEvent event) {}
    
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, QuicksandAPI.ID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, QuicksandAPI.ID);
    
    private static RegistryObject<PlacedFeature> registerPlacedFeature(String id,
                                                               RegistryObject<? extends ConfiguredFeature<?, ?>> feature,
                                                               PlacementModifier... modifiers) {
        return PLACED_FEATURES.register(id, () -> new PlacedFeature(((Holder<ConfiguredFeature<?, ?>>)feature.getHolder().get()), List.of(modifiers)));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    RegistryObject<ConfiguredFeature<FC, F>> registerConfiguredFeature(String id, F feature, FC featureConfig) {
        return registerConfiguredFeature(QuicksandAPI.loc(id), new ConfiguredFeature<>(feature, featureConfig));
    }
    
    @SuppressWarnings("unchecked")
    private static <CF extends ConfiguredFeature<?, ?>>
    RegistryObject<CF> registerConfiguredFeature(ResourceLocation id, CF value) {
    	return CONFIGURED_FEATURES.register(id.getPath(), () -> value);
    }
    
}
