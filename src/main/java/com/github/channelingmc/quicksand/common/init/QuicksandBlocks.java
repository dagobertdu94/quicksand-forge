package com.github.channelingmc.quicksand.common.init;

import com.github.channelingmc.quicksand.api.QuicksandAPI;
import com.github.channelingmc.quicksand.common.QuicksandConfigs;
import com.github.channelingmc.quicksand.common.block.QuicksandBlock;
import com.github.channelingmc.quicksand.common.block.QuicksandCauldronBlock;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = QuicksandAPI.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class QuicksandBlocks {
    
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
            helper.register(QuicksandAPI.QUICKSAND_BLOCK.getId(), new QuicksandBlock(
                BlockBehaviour.Properties.copy(Blocks.SAND),
                14406560,
                QuicksandAPI.QUICKSAND_BUCKET)
            );
            helper.register(QuicksandAPI.QUICKSAND_CAULDRON.getId(), new QuicksandCauldronBlock(
                BlockBehaviour.Properties.copy(Blocks.CAULDRON),
                QuicksandAPI.QUICKSAND_BUCKET,
                QuicksandAPI.QUICKSAND_INTERACTIONS)
            );
            helper.register(QuicksandAPI.RED_QUICKSAND_BLOCK.getId(), new QuicksandBlock(
                BlockBehaviour.Properties.copy(Blocks.RED_SAND),
                11098145,
                QuicksandAPI.RED_QUICKSAND_BUCKET)
            );
            helper.register(QuicksandAPI.RED_QUICKSAND_CAULDRON.getId(), new QuicksandCauldronBlock(
                BlockBehaviour.Properties.copy(Blocks.CAULDRON),
                QuicksandAPI.RED_QUICKSAND_BUCKET,
                QuicksandAPI.RED_QUICKSAND_INTERACTIONS)
            );
        });
    }
    
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            QuicksandAPI.QUICKSAND_CAULDRON.get().registerEmptyFillInteractions();
            QuicksandAPI.RED_QUICKSAND_CAULDRON.get().registerEmptyFillInteractions();
            if (QuicksandConfigs.COMMON.quicksandRenewable.get()) {
                CauldronInteraction.WATER.put(Items.SAND, QuicksandAPI.QUICKSAND_CAULDRON.get()::renewFromSand);
                CauldronInteraction.WATER.put(Items.RED_SAND, QuicksandAPI.RED_QUICKSAND_CAULDRON.get()::renewFromSand);
            }
        });
    }
    
}
