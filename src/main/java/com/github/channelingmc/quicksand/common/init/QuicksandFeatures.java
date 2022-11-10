package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.levelgen.feature.QuicksandLakeFeature;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandFeatures {
    
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.FEATURES, QuicksandAPI.QUICKSAND_LAKE.getId(), QuicksandLakeFeature::new);
    }
    
    @SubscribeEvent
    public static void registerGeneration(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            QuicksandAPI.QUICKSAND_LAKE_CF = registerConfiguredFeature("quicksand_lake",
                QuicksandAPI.QUICKSAND_LAKE.get(),
                new QuicksandLakeFeature.Configuration(
                    BlockStateProvider.simple(QuicksandAPI.QUICKSAND_BLOCK.get().defaultBlockState()),
                    BlockStateProvider.simple(Blocks.SAND.defaultBlockState())
                ));
            QuicksandAPI.RED_QUICKSAND_LAKE_CF = registerConfiguredFeature("red_quicksand_lake",
                QuicksandAPI.QUICKSAND_LAKE.get(),
                new QuicksandLakeFeature.Configuration(
                    BlockStateProvider.simple(QuicksandAPI.RED_QUICKSAND_BLOCK.get().defaultBlockState()),
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
        });
    }
    
    private static Holder<PlacedFeature> registerPlacedFeature(String id,
                                                               Holder<? extends ConfiguredFeature<?, ?>> feature,
                                                               PlacementModifier... modifiers) {
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, QuicksandAPI.loc(id),
            new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    Holder<ConfiguredFeature<FC, ?>> registerConfiguredFeature(String id, F feature, FC featureConfig) {
        return registerConfiguredFeature(QuicksandAPI.loc(id), new ConfiguredFeature<>(feature, featureConfig));
    }
    
    @SuppressWarnings("unchecked")
    private static <CF extends ConfiguredFeature<?, ?>>
    Holder<CF> registerConfiguredFeature(ResourceLocation id, CF value) {
        return (Holder<CF>) BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, id, value);
    }
    
}
